package com.dhcc.service;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 改名ins
 */
public class RenameINS {
    public static void main(String[] args) {
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\小蓝蓝\\littleblueplus402"; // 替换为实际文件夹路径

        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    //Long unixTimestamp = Long.valueOf(name.substring(14, IntStream.range(0,name.length()).filter(i -> name.charAt(i) == '_').skip(3).findFirst().orElse(-1)));
                    //Long unixTimestamp = Long.valueOf(name.substring(name.indexOf("_")+2, IntStream.range(0,name.length()).filter(i -> name.charAt(i) == '_').skip(3).findFirst().orElse(-1)));
                    Long unixTimestamp = Long.valueOf(name.split("_")[1]);
                    LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneId.systemDefault());

                    // 定义日期时间格式
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    // 格式化日期时间
                    String formattedDateTime = dateTime.format(formatter);

                    //System.out.println(formattedDateTime); // 输出：2024-5-2 18:58:04

                    int splitPos = name.length() - 4; // 倒数第4位的位置
                    String firstPart = name.substring(0, splitPos);
                    String secondPart = name.substring(splitPos);
                    String newName = "小蓝蓝_"+formattedDateTime+"_"+name.split("_")[2]+"_"+name.split("_")[3].substring(0,name.split("_")[3].length()-4)+"_INS"+secondPart;
                    //System.out.println(newName);
                    File newFile = new File(file.getParent(), newName);
                    if (file.renameTo(newFile)) {
                        System.out.println("成功修改文件名： " + file.getName() + " -> " + newFile);
                    } else {
                        System.out.println("修改文件名失败： " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("给定的路径不是一个文件夹");
        }
    }
}
