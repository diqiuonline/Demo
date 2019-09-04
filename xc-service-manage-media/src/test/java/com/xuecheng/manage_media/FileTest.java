/*
package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

*/
/**
 * User: 李锦卓
 * Time: 2019/4/28 15:07
 *//*

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileTest {
    @Test
    public void demo() throws Exception {
        FileWriter fileWriter = new FileWriter("a.txt");
        fileWriter.write("io你好");
        fileWriter.flush();
        fileWriter.close();
    }
    //测试文件分块
    @Test
    public void testChunk() throws Exception {
        //源文件
        File sourceFile = new File("d:/lucene.avi");
        //快文件目录
        String chunkFileFolder = "d:/chunks/";
        //定义快的大小
        long chunkFileSize = 1 * 1024 * 1024;
        //块数
        long chunkFileNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
        //创建读文件的对象
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        //缓冲区
        byte[] b = new byte[1024];
        for (int i = 0; i < chunkFileNum; i++) {
            //块文件
            File chunkFile = new File(chunkFileFolder + i);
            //创建向快文件的写对象
            RandomAccessFile rand_write = new RandomAccessFile(chunkFile, "rw");
            int len;
            while ((len = raf_read.read(b)) != -1) {
                rand_write.write(b, 0, len);
                //如果快文件的大小达到1m，开始写下一块
                if (chunkFile.length() >= chunkFileSize) {
                    break;
                }
            }
            rand_write.close();
        }
        raf_read.close();
    }
    //测试文件合并
    @Test
    public void testMergeFile() throws Exception {
        //快文件目录
        String chunkFileFolderPath = "d:/chunks/";
        //快文件目录对象
        File chunkFileFolder = new File(chunkFileFolderPath);
        //快文件列表
        File[] files = chunkFileFolder.listFiles();
        //将快文件排序，按名称升序
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });
        //合并文件
        File mergeFile = new File("d:/chunks/lucene.avi");
        //创建新文件
        boolean newFile = mergeFile.createNewFile();
        //创建写对象
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        byte[] b = new byte[1024];
        for (File chunkFile : fileList) {
            //读取一个快文件中的对象
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
            int len;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}*/
