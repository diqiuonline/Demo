package com.dhcc.service;

import java.io.File;

public class RenameXiezhen {

    public static void main(String[] args) {
        // 指定文件夹路径
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\新建文件夹 (2)\\古川kagura协整";

        // 获取文件夹下所有文件夹
        File folder = new File(folderPath);
        File[] folders = folder.listFiles(File::isDirectory);

        if (folders != null) {
            // 遍历每个文件夹
            for (File subFolder : folders) {
                String folderName = subFolder.getName();

                // 获取文件夹下所有文件
                File[] files = subFolder.listFiles(File::isFile);
                if (files != null) {
                    // 遍历每个文件
                    for (File file : files) {
                        String fileName = file.getName();
                        String extension = getFileExtension(fileName);

                        // 构建新文件名
                        String newFileName = folderName + "_" + getFileNameWithoutExtension(fileName) + "_写真." + extension;
                        File newFile = new File(subFolder.getAbsolutePath() + File.separator + newFileName);

                        // 重命名文件
                        if (file.renameTo(newFile)) {
                            System.out.println("文件重命名成功: " +fileName+"--->"+ newFile.getName());
                        } else {
                            System.out.println("文件重命名失败: " + file.getName());
                        }
                    }
                }
            }
        }
    }

    // 获取文件扩展名
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }

    // 获取文件名（不含扩展名）
    private static String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, dotIndex);
    }
}
