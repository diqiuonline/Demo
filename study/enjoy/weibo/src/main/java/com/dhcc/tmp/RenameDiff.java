package com.dhcc.tmp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RenameDiff {
    public static void main(String[] args) {
        Path folderA = Paths.get("D:\\VMOS\\菌烨\\weibo"); // 文件夹A的路径
        Path folderB = Paths.get("D:\\VMOS\\菌烨\\bak"); // 文件夹B的路径
        Path folderC = Paths.get("D:\\VMOS\\菌烨\\bak2"); // 文件夹C的路径
        Path folderD = Paths.get("D:\\VMOS\\小柔"); // 文件夹D的路径

        try {
            // 创建一个HashMap来存储文件夹A中文件的MD5和对应的Path
            Map<String, Path> md5MapA = new HashMap<>();
            Map<String, Path> md5MapB = new HashMap<>();
            Map<String, Path> md5MapC = new HashMap<>();
            Files.walk(folderA)
                    .filter(Files::isRegularFile) // 只过滤普通文件
                    .forEach(file -> {
                        try {
                            String md5 = calculateMd5(file.toFile());
                            md5MapA.put(md5, file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            Files.walk(folderB)
                    .filter(Files::isRegularFile) // 只过滤普通文件
                    .forEach(file -> {
                        try {
                            String md5 = calculateMd5(file.toFile());
                            md5MapB.put(md5, file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            Files.walk(folderC)
                    .filter(Files::isRegularFile) // 只过滤普通文件
                    .forEach(file -> {
                        try {
                            String md5 = calculateMd5(file.toFile());
                            md5MapC.put(md5, file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            // 遍历文件夹C，检查MD5并重命名文件
            Files.walk(folderD)
                    .filter(Files::isRegularFile) // 只过滤普通文件
                    .forEach(file -> {
                        try {
                            String md5 = calculateMd5(file.toFile());
                            if (md5MapA.containsKey(md5)) {
                                // 如果文件夹A中存在相同MD5的文件，移动文件夹B中的文件到文件夹C
                                String name = file.toFile().getName();
                                int splitPos = name.length() - 4; // 倒数第4位的位置
                                String firstPart = name.substring(0, splitPos);
                                String secondPart = name.substring(splitPos);
                                String newName = firstPart+"_微博"+secondPart;
                                renameFile(file.toFile(),newName);

                            }
                            if (md5MapB.containsKey(md5)) {
                                // 如果文件夹A中存在相同MD5的文件，移动文件夹B中的文件到文件夹C
                                String name = file.toFile().getName();
                                int splitPos = name.length() - 4; // 倒数第4位的位置
                                String firstPart = name.substring(0, splitPos);
                                String secondPart = name.substring(splitPos);
                                String newName = firstPart+"_推特"+secondPart;
                                renameFile(file.toFile(),newName);

                            }
                            if (md5MapC.containsKey(md5)) {
                                // 如果文件夹A中存在相同MD5的文件，移动文件夹B中的文件到文件夹C
                                String name = file.toFile().getName();
                                int splitPos = name.length() - 4; // 倒数第4位的位置
                                String firstPart = name.substring(0, splitPos);
                                String secondPart = name.substring(splitPos);
                                String newName = firstPart+"_小红书"+secondPart;
                                renameFile(file.toFile(),newName);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




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

    private static void renameFile(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (file.renameTo(newFile)) {
            System.out.println("File renamed successfully.");
        } else {
            System.out.println("Failed to rename file.");
        }
    }
}
