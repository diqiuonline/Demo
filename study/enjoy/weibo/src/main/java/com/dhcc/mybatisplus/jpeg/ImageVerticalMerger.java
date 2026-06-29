package com.dhcc.mybatisplus.jpeg;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class ImageVerticalMerger {

    public static void main(String[] args) {
        String inputDirPath = "C:\\Users\\nihaoa\\Desktop\\宝宝0-3随常见病症及食疗方";

        try {
            mergeImages(inputDirPath);
            System.out.println("图片拼接完成！");
        } catch (IOException e) {
            System.err.println("处理过程中发生错误: " + e.getMessage());
        }
    }

    public static void mergeImages(String inputDirPath) throws IOException {
        File dir = new File(inputDirPath);
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".jpg"));

        if (files == null || files.length == 0) {
            throw new IOException("目录中没有找到JPG文件");
        }

        // 按文件名排序
        Arrays.sort(files, Comparator.comparing(File::getName));

        int singleWidth = 1920;
        int singleHeight = 1440;
        int maxImagesPerMerge = 50;
        int totalImages = files.length;

        for (int i = 0; i < totalImages; i += maxImagesPerMerge) {
            int endIndex = Math.min(i + maxImagesPerMerge, totalImages);
            int currentMergeCount = endIndex - i;
            int totalHeight = singleHeight * currentMergeCount;

            BufferedImage result = new BufferedImage(singleWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = result.getGraphics();

            int yPosition = 0;
            for (int j = i; j < endIndex; j++) {
                System.out.printf("正在处理 %d/%d 张图片...\n", (j + 1), totalImages);
                BufferedImage img = ImageIO.read(files[j]);

                // 验证图片尺寸
                if (img.getWidth() != singleWidth || img.getHeight() != singleHeight) {
                    System.err.println("跳过尺寸不符的图片: " + files[j].getName());
                    continue;
                }

                g.drawImage(img, 0, yPosition, null);
                yPosition += singleHeight;
            }

            g.dispose();

            // 保存结果（建议使用PNG格式保证画质）
            String outputPath = "C:\\Users\\nihaoa\\Desktop\\新建文件夹\\output_" + (i / maxImagesPerMerge + 1) + ".png";
            ImageIO.write(result, "png", new File(outputPath));
            PngQuantCompressor.start(outputPath,outputPath);
        }
    }
}