package com.dhcc.tmp;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class bak {

    private static final ExecutorService executor = Executors.newFixedThreadPool(30);
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(30, 5, TimeUnit.MINUTES)) // 创建一个新的连接池，最大空闲连接数为5，保持空闲连接5分钟
            .build();

    public static void main(String[] args) {
        String filePath = "D:\\VMOS\\linux\\pc-192-168-2-115\\redss\\bak\\src.txt";
        String filePath2 = "D:\\VMOS\\linux\\pc-192-168-2-115\\redss\\bak\\src12.txt";

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath)).stream().map(String::trim).collect(Collectors.toList());


            List<String> specifiedStrings = Arrays.asList("data-preview-url");
            lines = lines.stream()
                    .filter(s -> specifiedStrings.stream().anyMatch(s::contains))
                    .map(s -> s.substring(s.indexOf("data-preview-url")+18, s.lastIndexOf("data-index")-2)) // 对每个字符串进行截取
                    .distinct()
                    .collect(Collectors.toList());


            System.out.println("下载图片数量"+lines.size());



                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath2))) {
                    for (String item : lines) {
                        // 写入列表中的每一项后跟换行符
                        writer.write(item);
                        writer.newLine(); // 使用newLine()方法添加换行，跨平台兼容
                    }
                    System.out.println("数据已成功写入到文件中！");
                } catch (IOException e) {
                    // 处理可能发生的IO异常
                    e.printStackTrace();
                    System.err.println("写入文件时发生错误！");
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
            // 发生异常时取消所有任务并关闭线程池
        }
    }
}
