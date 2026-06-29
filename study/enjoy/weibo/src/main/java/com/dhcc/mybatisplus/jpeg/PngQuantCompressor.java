package com.dhcc.mybatisplus.jpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PngQuantCompressor {

    public static void compressPng(File inputFile, File outputFile, int minQuality, int maxQuality) throws IOException, InterruptedException {
        // 确保输出文件的父目录存在
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs();
        }

        // 构建命令参数
        ProcessBuilder pb = new ProcessBuilder(
                "D:\\Program Files\\pngquant\\pngquant.exe",
                "--quality", minQuality + "-" + maxQuality,
                "--force", // 覆盖已存在的输出文件
                "--output", outputFile.getAbsolutePath(),
                inputFile.getAbsolutePath()
        );

        // 可选：设置工作目录（若需要）
        // pb.directory(new File("指定目录"));

        // 合并错误流到标准输出流，便于读取
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // 读取命令输出（避免阻塞）
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[pngquant] " + line); // 打印日志
            }
        }

        // 等待进程结束
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("pngquant压缩失败，退出码: " + exitCode);
        }

        System.out.println("压缩成功，输出文件: " + outputFile.getAbsolutePath());
    }

    public static void start(String inputpath, String outputpath) {
        File input = new File(inputpath);
        File output = new File(outputpath);

        try {
            compressPng(input, output, 10, 10);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}