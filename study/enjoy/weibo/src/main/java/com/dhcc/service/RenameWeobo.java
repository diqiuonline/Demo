package com.dhcc.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 微博 改名
 */
public class RenameWeobo {

    public static void main(String[] args) {
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\走路摇\\weibo"; // 替换为你的文件夹路径

        File directory = new File(folderPath);
        File[] files = directory.listFiles();

        if (files != null) {
            // 创建一个固定数量的线程池
            ExecutorService executor = Executors.newFixedThreadPool(4); // 这里选择4个线程，可以根据需要调整

            for (File file : files) {
                if (file.isFile() && !file.getName().contains("走路摇_") && file.getName().contains("T")) {
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
        String truncatedString = file.getName().substring(0, 10).substring(0, 8);
        String date = formatDateString(truncatedString);

        String result = null;
        int index = file.getName().indexOf("T_") + "T_".length();

        if (index != -1 && index < file.getName().length()) {
            result = file.getName().substring(index);
        } else {
            System.out.println("未找到符合条件的子字符串");
            return;
        }

        String newName = "走路摇_" + date + "_" + result.substring(0,result.indexOf('.')) + "_weibo" +result.substring(result.length()-4);
        renameFile(file, newName);
        //System.out.println(file.getName()+"----------->"+newName);
    }

    private static String formatDateString(String createdAt) {
        String outputDateFormat = "yyyy-MM-dd";
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = null;
        try {
            Date date = inputDateFormat.parse(createdAt);
            SimpleDateFormat outputDateFormatObj = new SimpleDateFormat(outputDateFormat);
            formattedDate = outputDateFormatObj.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
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
