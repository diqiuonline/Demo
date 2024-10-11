package com.dhcc.tmp;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

/**
 * 删除重复文件
 */
public class RemoveDuplicateFiles {
    public static void main(String[] args) {
        String directoryPath = "D:\\VMOS\\新建文件夹\\猫梓子\\images"; // 替换为你的目录路径
        removeDuplicateFiles(directoryPath);
    }

    public static void removeDuplicateFiles(String directoryPath) {
        try {
            Map<String, File> md5Map = new HashMap<>();

            Files.walk(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            String md5 = getMD5(file);
                            if (md5Map.containsKey(md5)) {
                                // 删除重复文件
                                System.out.println("Deleting duplicate file: " + file.getFileName()+""+md5Map.get(md5).toString());
                                try {
                                    Files.delete(file);
                                } catch (IOException e) {
                                    System.err.println("Failed to delete file: " + file.getFileName());
                                    e.printStackTrace();
                                }
                            } else {
                                md5Map.put(md5, file.toFile());
                            }
                        } catch (IOException | NoSuchAlgorithmException e) {
                            System.err.println("Error processing file: " + file.getFileName());
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMD5(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : md5sum) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }
}
