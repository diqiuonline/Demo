package com.dhcc.mybatisplus.jpeg;

import javax.imageio.*;
import javax.imageio.stream.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class JpegCompressor {
    public static void compressJPEG(File inputFile, File outputFile, float quality) throws IOException {
        // 读取原始图片
        BufferedImage image = ImageIO.read(inputFile);

        // 获取JPEG图片写入器
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

        // 配置压缩参数
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality); // 质量范围0.0-1.0（低到高）

        // 写入压缩后的图片
        try (ImageOutputStream output = ImageIO.createImageOutputStream(outputFile)) {
            writer.setOutput(output);
            writer.write(null, new IIOImage(image, null, null), param);
        }
        writer.dispose();
    }

    public static void main(String[] args) {
        try {
            compressJPEG(new File("D:\\Develop\\response\\Demo\\study\\enjoy\\weibo\\output_1.jpg"), new File("D:\\Develop\\response\\Demo\\study\\enjoy\\weibo\\output_1_yasuo.jpg"), 0.75f); // 50%质量
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}