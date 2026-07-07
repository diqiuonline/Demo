package com.dhcc.service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class SyncFolderTimeWithLargestFile {

    public static void main(String[] args) {
        // 1. 指定你的目标根目录 (请替换为实际的 Windows 路径，例如 "D:\\TestFolder" 或 "D:/TestFolder")
        Path rootDirectory = Paths.get("F:\\nihaoa\\Downloads\\115\\佐々木さき");

        if (!Files.exists(rootDirectory) || !Files.isDirectory(rootDirectory)) {
            System.err.println("指定的目录不存在或不是一个文件夹: " + rootDirectory);
            return;
        }

        System.out.println("开始扫描目录: " + rootDirectory);

        // 2. 获取根目录下的所有直接子文件夹
        try (Stream<Path> subDirs = Files.list(rootDirectory)) {
            subDirs.filter(Files::isDirectory)
                    .forEach(SyncFolderTimeWithLargestFile::processSubdirectory);

            System.out.println("处理完成！");
        } catch (IOException e) {
            System.err.println("读取根目录失败: " + e.getMessage());
        }
    }

    /**
     * 处理单个子文件夹：找到最大文件并修改文件夹时间
     */
    private static void processSubdirectory(Path folder) {
        // 使用 Files.walk 递归遍历该文件夹下的所有文件（包含其内部的子目录）
        try (Stream<Path> files = Files.walk(folder)) {

            // 过滤出普通文件（排除文件夹），并按文件大小找最大值
            Optional<Path> largestFileOpt = files.filter(Files::isRegularFile)
                    .max(Comparator.comparingLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            // 遇到无法读取大小的文件时忽略
                            return -1L;
                        }
                    }));

            if (largestFileOpt.isPresent()) {
                Path largestFile = largestFileOpt.get();
                FileTime maxFileTime = Files.getLastModifiedTime(largestFile);
                long maxSize = Files.size(largestFile);

                // 将子文件夹的修改时间设置为最大文件的修改时间
                Files.setLastModifiedTime(folder, maxFileTime);

                System.out.printf("✅ 成功: 文件夹 [%s] -> 时间已同步为 %s (最大文件: %s, 大小: %d byte)\n",
                        folder.getFileName(), maxFileTime, largestFile.getFileName(), maxSize);
            } else {
                System.out.printf("⚠️ 跳过: 文件夹 [%s] 为空或没有找到任何文件。\n", folder.getFileName());
            }

        } catch (IOException e) {
            System.err.println("❌ 处理文件夹 [" + folder.getFileName() + "] 时发生错误: " + e.getMessage());
        }
    }
}