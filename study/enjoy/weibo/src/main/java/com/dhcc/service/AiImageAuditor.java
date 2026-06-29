package com.dhcc.service;

import ai.djl.Application;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.Joints;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class AiImageAuditor {

    private static final String SOURCE_DIR = "D:\\VMOS\\windows\\pc-192-168-2-115\\caches\\AAedss\\output\\123";
    private static final String BAD_DIR = "D:\\VMOS\\windows\\pc-192-168-2-115\\caches\\AAedss\\output\\新建文件夹";

    // ================= 严格的黑名单阈值 =================
    // 只有置信度极高时，才判定为"存在"，避免把背景看成手
    private static final double HIGH_CONFIDENCE = 0.55;

    // 畸形判定阈值：一只手是另一只手的 2.2 倍长，人类不可能做到（透视除外，下面有算法处理透视）
    private static final double MAX_LIMB_DIFF_RATIO = 2.2;

    // 躯干比例异常：手臂总长如果超过身高的 1.3倍（长臂猿）
    private static final double MAX_ARM_BODY_RATIO = 1.3;

    public static void main(String[] args) {
        System.setProperty("sun.jnu.encoding", "UTF-8");

        // 显存优化：3060 12G 足够跑大模型，但需要设置一下
        System.setProperty("ai.djl.pytorch.num_interop_threads", "4");

        try {
            Files.createDirectories(Paths.get(BAD_DIR));

            // 1. 升级模型：使用 ResNet101 或 ResNet152，精度远高于 ResNet18
            Criteria<Image, Joints> criteria = Criteria.builder()
                    .optApplication(Application.CV.POSE_ESTIMATION)
                    .setTypes(Image.class, Joints.class)
                    // 关键点：使用 resnet101 (更深的网络，更能理解复杂姿势)
                    .optFilter("backbone", "resnet101")
                    .optFilter("dataset", "coco")
                    .optEngine("MXNet") // 或 "PyTorch"，取决于你的pom依赖
                    .optProgress(new ProgressBar())
                    .build();

            try (ZooModel<Image, Joints> model = criteria.loadModel();
                 Predictor<Image, Joints> predictor = model.newPredictor()) {

                System.out.println("=== 高精度模型(ResNet101)加载完毕 | 模式：仅拦截严重畸形 ===");

                try (Stream<Path> stream = Files.walk(Paths.get(SOURCE_DIR))) {
                    stream.filter(Files::isRegularFile)
                            .filter(p -> p.toString().matches(".*\\.(jpg|jpeg|png|bmp)$"))
                            .forEach(path -> detectAndMove(path, predictor));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void detectAndMove(Path imagePath, Predictor<Image, Joints> predictor) {
        try {
            Image img = loadImage(imagePath);
            if (img == null) return;

            Joints joints = predictor.predict(img);

            // 核心改变：detectAnatomyFailure 返回 true 表示"一定是畸形"，否则全部放行
            boolean isMonster = detectAnatomyFailure(joints, img.getWidth(), img.getHeight());

            if (isMonster) {
                System.out.println("[REJECT - 畸形] " + imagePath.getFileName());
                moveToFolder(imagePath, BAD_DIR);
            } else {
                // 默认放行所有"正常"或"无法判断"的照片
                System.out.println("[PASS - 正常] " + imagePath.getFileName());
            }

        } catch (Exception e) {
            System.err.println("Skip: " + imagePath + " " + e.getMessage());
        }
    }

    /**
     * 核心逻辑：寻找"生物学不可能"的特征
     * 不再检查"是否完整"，只检查"是否离谱"
     */
    private static boolean detectAnatomyFailure(Joints joints, int width, int height) {
        List<Joints.Joint> list = joints.getJoints();
        if (list.isEmpty()) return false; // 没检测到人，放行

        // COCO 关键点索引：
        // 0:Nose, 1:LEye, 2:REye, 3:LEar, 4:REar
        // 5:LShoulder, 6:RShoulder, 7:LElbow, 8:RElbow, 9:LWrist
        // 11:LHip, 12:RHip, 13:LKnee, 14:RKnee, 15:LAnkle, 16:RAnkle

        Joints.Joint lShoulder = list.get(5);
        Joints.Joint rShoulder = list.get(6);
        Joints.Joint lHip = list.get(11);
        Joints.Joint rHip = list.get(12);

        // --- 检查 1: 拓扑结构错乱 (例如：胯部在肩膀上面) ---
        // 只有在是直立姿态（非倒立）时才检查。如何判断直立？Nose 在 Shoulder 上方
        Joints.Joint nose = list.get(0);
        boolean isUpright = isConfident(nose) && isConfident(lShoulder) && nose.getY() < lShoulder.getY();

        if (isUpright && isConfident(lHip) && isConfident(lShoulder)) {
            // 如果胯部比肩膀还高（Y坐标更小），绝对是畸形
            if (lHip.getY() < lShoulder.getY()) return true;
        }

        // --- 检查 2: 极度不对称 (针对长短手) ---
        // 只有当 左臂 和 右臂 的所有关节都极其清晰时，才进行比较
        if (isArmVisible(list, true) && isArmVisible(list, false)) {
            double lArmLen = getArmLength(list, true);
            double rArmLen = getArmLength(list, false);

            // 计算双肩倾斜度，如果是严重侧身或透视（肩膀连线很短），则不比较手臂
            double shoulderWidth = getDist(lShoulder, rShoulder);
            if (shoulderWidth > width * 0.1) { // 肩膀有一定宽度，说明基本是正面
                double max = Math.max(lArmLen, rArmLen);
                double min = Math.min(lArmLen, rArmLen);

                // 如果一只手长度是另一只的 2.2 倍以上，判定为畸形
                if (min > 0 && max / min > MAX_LIMB_DIFF_RATIO) {
                    return true;
                }
            }
        }

        // --- 检查 3: 比例崩坏 (长臂猿检测) ---
        // 身体核心高度 (躯干长度)
        if (isConfident(lShoulder) && isConfident(lHip)) {
            double torsoLen = getDist(lShoulder, lHip);
            double lArmLen = getArmLength(list, true);

            // 如果单只手臂长度 > 躯干长度的 2.5 倍 (正常人约 1.2-1.5)，判定为畸形
            if (torsoLen > 0 && lArmLen > torsoLen * 2.5) {
                return true;
            }
        }

        // --- 检查 4: 关节极度扭曲 (折叠人) ---
        // 检查膝盖是否反向折叠 (需要复杂的向量计算，这里用简单的坐标检查)
        // 如果脚踝的 Y 坐标 小于 膝盖 (脚比膝盖高)，且是在站立模式下 -> 异常
        Joints.Joint lKnee = list.get(13);
        Joints.Joint lAnkle = list.get(15);
        if (isUpright && isConfident(lKnee) && isConfident(lAnkle) && isConfident(lHip)) {
            // 简单判断：如果脚踝在膝盖上面，且膝盖在屁股下面 (并没有在做高抬腿)
            // 这里为了防止误杀瑜伽动作，阈值设得非常宽松
            if (lAnkle.getY() < lKnee.getY() && Math.abs(lAnkle.getX() - lHip.getX()) < width * 0.1) {
                // 垂直方向脚踝反超膝盖，且X轴很接近 -> 可能是小腿反折
                // 此处容易误杀，建议保守，可先注释掉
            }
        }

        return false;
    }

    // 辅助：检查整条手臂是否清晰可见
    private static boolean isArmVisible(List<Joints.Joint> list, boolean isLeft) {
        int offset = isLeft ? 0 : 1;
        // Shoulder(5/6), Elbow(7/8), Wrist(9/10)
        return list.get(5 + offset).getConfidence() > HIGH_CONFIDENCE &&
                list.get(7 + offset).getConfidence() > HIGH_CONFIDENCE &&
                list.get(9 + offset).getConfidence() > HIGH_CONFIDENCE;
    }

    // 辅助：获取手臂长度
    private static double getArmLength(List<Joints.Joint> list, boolean isLeft) {
        int offset = isLeft ? 0 : 1;
        return getDist(list.get(5 + offset), list.get(7 + offset)) + // Upper arm
                getDist(list.get(7 + offset), list.get(9 + offset));  // Lower arm
    }

    private static boolean isConfident(Joints.Joint j) {
        return j != null && j.getConfidence() > HIGH_CONFIDENCE;
    }

    private static double getDist(Joints.Joint p1, Joints.Joint p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    private static Image loadImage(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            return ImageFactory.getInstance().fromInputStream(is);
        } catch (Exception e) {
            BufferedImage bi = ImageIO.read(path.toFile());
            return bi == null ? null : ImageFactory.getInstance().fromImage(bi);
        }
    }

    private static void moveToFolder(Path source, String targetDir) {
        try {
            Path target = Paths.get(targetDir).resolve(source.getFileName());
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Move failed: " + e.getMessage());
        }
    }
}