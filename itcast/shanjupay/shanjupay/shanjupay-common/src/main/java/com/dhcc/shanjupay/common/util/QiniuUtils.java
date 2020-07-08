package com.dhcc.shanjupay.common.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.EncodingException;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/** 七牛云测试工具类
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/16 15:18
 */

public class QiniuUtils {
    private static final Logger log = LoggerFactory.getLogger(QiniuUtils.class);
    /**
     *
     * @param accessKey ak
     * @param secretKey sk
     * @param bucket    空间名
     * @param uploadBytes 文件字节数据
     * @param key       文件名 七牛云上的文件名
     */
    public static void upload2qiniu(String accessKey, String secretKey, String bucket, byte[] uploadBytes, String key) throws RuntimeException{

        //构造一个带指定 Region 对象的配置类  和申请的地地方保持一致
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            //上传参数  字节数据 key token令牌
            Response response = uploadManager.put(uploadBytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
        } catch (Exception ex) {
            log.error("上传文件到七牛：" + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

   //测试文件上传
    private static void testUpload() {
        //构造一个带指定 Region 对象的配置类  和申请的地地方保持一致
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "42FjT8A6B_nU_uN4Pxg1EB7IgB3aT9zTeIMQz7Pc";
        String secretKey = "kGlpVQ1SfCl0jFNGiuSI-DdQTX4ywnrky-u9RAMb";
        String bucket = "diqiuonline";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = String.valueOf(UUID.randomUUID())+".jpg";
        FileInputStream fileInputStream = null;
        try {
            //得到本地文件的字节数据
            String filePath = "E:\\新建文件夹\\微信图片_20200705120138.png";
            fileInputStream = new FileInputStream(new File(filePath));
            //得到本地的字节文件数据
            byte[] uploadBytes = IOUtils.toByteArray(fileInputStream);
            //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                //上传参数  字节数据 key token令牌
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (
                Exception ex) {
            //ignore
        }
    }
    private static void textDownloader() {
        try {
            String fileName = "b4a5cfd2-c2c3-4b9b-a28e-96eebc8e40e7.jpg";
            String domainOfBucket = "http://qc0b4tqmh.bkt.clouddn.com";
            String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
            String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
            String accessKey = "42FjT8A6B_nU_uN4Pxg1EB7IgB3aT9zTeIMQz7Pc";
            String secretKey = "kGlpVQ1SfCl0jFNGiuSI-DdQTX4ywnrky-u9RAMb";
            Auth auth = Auth.create(accessKey, secretKey);
            long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
            String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
            System.out.println(finalUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        testUpload();
        //textDownloader();
    }
}
