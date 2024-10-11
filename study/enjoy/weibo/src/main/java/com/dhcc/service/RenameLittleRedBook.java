package com.dhcc.service;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 小红书改名
 */
public class RenameLittleRedBook {
    public static void main(String[] args) {
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\小蓝蓝\\居家小蓝蓝_2024-09-11_18-01-05"; // 替换为你的文件夹路径

        File directory = new File(folderPath);
        File[] files = directory.listFiles();

        if (files != null) {
            // 创建一个固定数量的线程池
            ExecutorService executor = Executors.newFixedThreadPool(4); // 这里选择4个线程，可以根据需要调整

            for (File file : files) {
                if (file.isFile() ) {
                    // 提交文件处理任务给线程池
                    executor.execute(() -> processFile(file));
                }
            }

            // 关闭线程池
            executor.shutdown();
        } else {
            System.out.println("The directory is empty or does not exist.");
        }
    }

    private static void processFile(File file) {
        String str = file.getName();
        String part3 = str.substring(str.indexOf('_')+1,str.lastIndexOf('.'));
        String newStr = "小蓝蓝_" + part3+"_小红书"+str.substring(str.lastIndexOf('.'),str.length());
        //System.out.println(str+"---------->"+newStr);
        renameFile(file, newStr);
    }



    private static void renameFile(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (file.renameTo(newFile)) {
            System.out.println("File renamed successfully: " + file.getName() + " -> " + newName);
        } else {
            System.out.println("Failed to rename file: " + file.getName());
        }
    }
}
