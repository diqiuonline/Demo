package com.dhcc.tmp;

import java.io.*;
import java.nio.file.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 比较md5 移动文件
 */
public class FileMd5Move {

    // 计算文件的MD5值  
    public static String calculateMd5(File file) throws NoSuchAlgorithmException, IOException {
        try (InputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance("MD5"))) {

            byte[] buffer = new byte[1024];
            while (dis.read(buffer) != -1) {
                // 读取数据以计算MD5  
            }

            byte[] digest = dis.getMessageDigest().digest();
            return bytesToHex(digest);
        }
    }

    // 将字节数组转换为十六进制字符串  
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // 主方法，执行文件比较和移动操作  
    public static void main(String[] args) {
        Path folderA = Paths.get("D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\一小央泽\\weibo\\2019-06-29_微博 [660P-635M]"); // 文件夹A的路径
        Path folderB = Paths.get("D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\一小央泽\\weibo\\一小央泽 - 微博配图 [164P 517M]"); // 文件夹B的路径
        Path folderC = Paths.get("D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\一小央泽\\weibo\\新建文件夹"); // 文件夹C的路径

        try {
            // 创建一个HashMap来存储文件夹A中文件的MD5和对应的Path  
            Map<String, Path> md5Map = new HashMap<>();
            Files.walk(folderA)
                    .filter(Files::isRegularFile) // 只过滤普通文件  
                    .forEach(file -> {
                        try {
                            String md5 = calculateMd5(file.toFile());
                            md5Map.put(md5, file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

            // 遍历文件夹B，检查MD5并移动文件到文件夹C  
            Files.walk(folderB)
                    .filter(Files::isRegularFile) // 只过滤普通文件  
                    .forEach(file -> {
                        try {
                            String md5 = calculateMd5(file.toFile());
                            if (md5Map.containsKey(md5)) {
                                // 如果文件夹A中存在相同MD5的文件，移动文件夹B中的文件到文件夹C
                                Path target = folderC.resolve(file.getFileName());
                                Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("a文件夹文件"+md5Map.get(md5)+"Moved " + file + " to " + target);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}