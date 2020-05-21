package com.xuecheng.manage_media_process;

import com.xuecheng.framework.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {

    @Test
    public void testProcessBuilder() throws IOException {
        //使用processBuilder来调用第三方应用程序
        ProcessBuilder processBuilder = new ProcessBuilder();
        //设置第三方应用程序命令
        processBuilder.command("ipconfig");
        //将标准输入流和错误流合并
        processBuilder.redirectErrorStream(true);
        //启动一个进程
        Process start = processBuilder.start();
        //通过标准输入流拿到正常和错误信息
        InputStream inputStream = start.getInputStream();
        //转换成字符流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
        char[] b = new char[1024];
        int len ;
        while ((len = inputStreamReader.read(b)) != -1) {
            String s = new String(b, 0, len);
            System.out.println(s);
        }
        inputStream.close();
        inputStreamReader.close();


    }

    //测试使用工具类将avi转成mp4
    @Test
    public void testProcessMp4(){
        String ffmpeg_path = "D:/Develop/ffmpeg/ffmpeg-20180227-fa0c9d6-win64-static/bin/ffmpeg.exe";
        String video_path = "E:\\Study\\JavaEE\\视频\\就业班第八阶段\\学成在线项目\\day14 媒资管理\\资料\\资料\\lucene.avi";
        String mp4_name = "lucene.mp4";
        String mp4folder_path = "E:\\Study\\JavaEE\\视频\\就业班第八阶段\\学成在线项目\\day14 媒资管理\\资料\\资料\\";
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4folder_path);
        String s = mp4VideoUtil.generateMp4();
        System.out.println(s);
    }

}
