package com.xuecheng.test.fastdfs;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {
    //上传文件
    @Test
    public void testUpload(){
        try {
            int a = 1;

            //加载配置文件fastdfs-client.properties
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义trackerClient 用于请求trackerServer
            TrackerClient trackerClient = new TrackerClient();
            //链接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取stroge
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建storageClient
            StorageClient1 storageClient1 = new StorageClient1();
            //想storageServer服务器上传文件
            //本地文件路径
            String filePath = "d:/d.jpg";
            //上传成功后拿到文件ID
            String fileId = storageClient1.upload_file1(filePath, "jpg", null);
            System.out.println(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //查询文件
    @Test
    public void testFind(){
        try {
            //加载配置文件fastdfs-client.properties
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义trackerClient 用于请求trackerServer
            TrackerClient trackerClient = new TrackerClient();
            //链接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取stroge
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            //下载文件s
            String fileId = "group1/M00/00/00/wKgCaV3X6YeANV9pAI1wT8tVVg0120.jpg";
            byte[] bytes = storageClient1.download_file1(fileId);
            //使用输出流保存文件
            FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/logo.jpg"));
            fileOutputStream.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}