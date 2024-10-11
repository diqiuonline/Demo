package com.dhcc.tmp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class RemoveFirstCharsFromFile {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("D:\\VMOS\\菌烨\\bak3\\webp.bash"); // 原始文件路径
        Path outputFilePath = Paths.get("D:\\VMOS\\菌烨\\bak3\\webp2.bash"); // 新文件路径
        int numCharsToRemove = 9; // 想要删除的字符数

        try {
            List<String> lines = Files.readAllLines(inputFilePath);
            List<String> modifiedLines = new ArrayList<>();

            for (String line : lines) {
                if (line.length() >= numCharsToRemove) {
                    modifiedLines.add(line.substring(numCharsToRemove));
                } else {
                    modifiedLines.add(line); // 如果行长度小于要删除的字符数，则保留原行  
                }
            }

            // 将修改后的行写入新文件  
            Files.write(outputFilePath, modifiedLines, StandardCharsets.UTF_8);

            System.out.println("修改后的行已成功写入新文件！");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}