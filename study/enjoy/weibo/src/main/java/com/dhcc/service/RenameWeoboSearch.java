package com.dhcc.service;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微博search 改名
 */
public class RenameWeoboSearch {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(30, 5, TimeUnit.MINUTES)) // 创建一个新的连接池，最大空闲连接数为5，保持空闲连接5分钟
            .build();

    public static void main(String[] args) {
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\是依酱呀\\依依超级懒\\img"; // 替换为你的文件夹路径

        File directory = new File(folderPath);
        File[] files = directory.listFiles();

        if (files != null) {
            ExecutorService executor = Executors.newFixedThreadPool(20); // 创建一个固定大小的线程池
            for (File file : files) {
                if (file.isFile() && !file.getName().contains("是依酱呀")) { // 确保是文件，而且不包含"小柔"
                    executor.submit(new FileProcessor(file)); // 提交任务到线程池
                }
            }
            executor.shutdown(); // 关闭线程池
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // 等待所有任务完成
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The directory is empty or does not exist.");
        }
    }
    static class FileProcessor implements Runnable {
        private File file;

        public FileProcessor(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            String weiboId = extractWeiboId(file.getName());

            String responseBody = fetchResponseBody(client, weiboId);
            if (!responseBody.isEmpty()) {
                String createdAt = extractCreatedAt(responseBody);
                if (!createdAt.isEmpty()) {
                    String formattedDateString = formatDateString(createdAt);
                    if (!formattedDateString.isEmpty()) {
                        String newName = "是依酱呀_" + formattedDateString + "_" + file.getName().substring(0,file.getName().indexOf('.')) + "_weibo" +file.getName().substring(file.getName().length()-4);
                        renameFile(file, newName);
                    }
                }
            }
        }
    }
    private static String extractWeiboId(String originalString) {
        int dashIndex = originalString.indexOf('-');
        if (dashIndex != -1) {
            return originalString.substring(0, dashIndex);
        } else {
            return originalString.substring(0, originalString.length() - 4);
        }
    }

    private static String fetchResponseBody(OkHttpClient client, String weiboId) {
        String responseBody = "";
        Request request = new Request.Builder()
                .url("https://m.weibo.cn/detail/" + weiboId) // 替换为你想要请求的URL
                .build(); // 创建Request对象
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseBody = response.body().string();
            } else {
                System.out.println("Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    private static String extractCreatedAt(String responseBody) {
        Pattern pattern = Pattern.compile("\"created_at\":\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(responseBody);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            System.out.println("未找到匹配项");
            return "";
        }
    }

    private static String formatDateString(String createdAt) {
        SimpleDateFormat parser = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        parser.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        try {
            Date date = parser.parse(createdAt);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void renameFile(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (file.renameTo(newFile)) {
            System.out.println("File renamed successfully.");
        } else {
            System.out.println("Failed to rename file.");
        }
    }
}
