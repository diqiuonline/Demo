package com.dhcc.tmp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Deletelongjpg {
    public static void main(String[] args) {
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\rioko凉凉子\\肉扣热热子\\img\\转发微博图片"; // 替换为实际文件夹路径
        String target = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\rioko凉凉子\\新建文件夹";
        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            ExecutorService executorService = Executors.newFixedThreadPool(32); // 使用可用处理器数量作为线程池大小
            List<Future<Void>> futures = new ArrayList<>();

            for (File file : files) {
                Callable<Void> task = () -> {
                    processImage(file, target);
                    return null;
                };
                futures.add(executorService.submit(task));
            }

            // 等待所有任务完成
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("执行任务时发生错误: " + e.getMessage());
                }
            }

            executorService.shutdown(); // 关闭线程池
        } else {
            System.out.println("给定的路径不是一个文件夹");
        }
    }

    private static void processImage(File file, String target) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            if (bufferedImage == null) {
                throw new IOException("无法读取图片: " + file.getName()); // 明确处理读取失败的情况
            }
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (height / (double)width > 2) {
                System.out.println(file.getName() + "--->图片分辨率: " + width + "x" + height + "是长图片");
                Path source = Paths.get(file.getAbsolutePath());
                Path destination = Paths.get(target, file.getName());
                Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.println("处理图片时发生错误: " + file.getName() + " - " + e.getMessage()); // 在异常信息中包含文件名
        }
    }
}