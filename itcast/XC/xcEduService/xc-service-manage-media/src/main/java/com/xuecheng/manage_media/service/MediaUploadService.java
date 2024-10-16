package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * User: 李锦卓
 * Time: 2019/5/2 15:33
 */
@Service
@Transactional
public class MediaUploadService {
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Value("${xc-service-manage-media.upload-location}")
    private String upload_location;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    String routingkey_media_video;

    //得到文件所属路径
    private String getFileFolderPath(String fileMd5) {
        return upload_location + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
    }

    //得到文件的路径
    private String getFilePath(String fileMd5,String fileExt) {
        return this.getFileFolderPath(fileMd5) + fileMd5 + "." + fileExt;
    }

    //得到快文件所属目录路径
    private String getChunkFileFolderPath(String fileMd5) {
        return this.getFileFolderPath(fileMd5) + "chunk/";
    }

    /**
     * 文件上传前注册 检查文件是否存在
     * 根据md5得到文件路径
     * 规则 一级目录 md5的第一个字符 二级目录 md5的第二个字符 三级目录 md5
     * 文件名 md5+文件扩展名
     * @param fileMd5 文件的md5
     * @param fileName 文件的名字
     * @param fileSize 文件的大小
     * @param mimetype 文件的类型
     * @param fileExt 文件的扩展名
     * @return
     */
    public ResponseResult regist(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //1检查文件在磁盘上是否存在
        //文件所属目录的路径
        String fileFolderPath = this.getFileFolderPath(fileMd5);
        //文件的路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //文件是否存在
        boolean exists = file.exists();
        //2检查文件信息在mongdb中是否存在
        Optional<MediaFile> byId = mediaFileRepository.findById(fileMd5);
        if (exists && byId.isPresent()) {
            //文件存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //文件不存在时做一些准备工作，检查文件所在目录是否存在，如果不存在则创建
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     *分块检查
     * @param fileMd5 文件md5
     * @param chunk chunk的下标
     * @return
     */
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk) {
        //检查分块文件是否存在
        //得到分块文件的所在目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        //快文件
        File chunkFile = new File(chunkFileFolderPath + chunk);
        if (chunkFile.exists()) {
            //快文件存在
            return new CheckChunkResult(CommonCode.SUCCESS, true);
        } else {
            //快文件不存在
            return new CheckChunkResult(CommonCode.SUCCESS, false);
        }
    }

    /**
     * 上传分块
     * @param file 所上传文件
     * @param fileMd5 文件md5
     * @param chunk 快文件下标
     * @return
     */
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        //检查快文件目录如果不存在则创建
        //得到分块目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        //得到分块文件路劲
        String chunkFilePath = chunkFileFolderPath + chunk;
        File chunkFileFolder = new File(chunkFileFolderPath);
        //如果不存在 创建
        if (!chunkFileFolder.exists()){
            chunkFileFolder.mkdirs();
        }
        //得到上传文件的输入流
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(new File(chunkFilePath));
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 合并文件
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //1合并所有分块
        //得到分块所属目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        //分块文件列表
        File[] files = chunkFileFolder.listFiles();
        List<File> fileList = Arrays.asList(files);
        //创建一个合并文件
        String chunkFile = this.getFilePath(fileMd5, fileExt);
        File mergeFile = new File(chunkFile);
        //执行合并
        mergeFile = this.mergeFile(fileList, mergeFile);
        if (mergeFile == null) {
            //合并文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        //检验文件的md5值是否和前端传入的md5值相同
        boolean checkFileMd5 = this.checkFileMd5(mergeFile, fileMd5);
        if (!checkFileMd5) {
            //校验文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        //将文件的信息写入mongdb
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        //文件路径保存相对路径
        String filePath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        mediaFile.setFilePath(filePath);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save(mediaFile);
        //发送视频处理消息
        this.sendProcessVideoMsg(mediaFile.getFileId());
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //想mq发送消息
    public ResponseResult sendProcessVideoMsg(String mediaId) {
        //查询数据库mediaFile
        Optional<MediaFile> byId = mediaFileRepository.findById(mediaId);
        if (!byId.isPresent()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //构建消息内容
        Map<String, String> map = new HashMap<>();
        map.put("mediaId", mediaId);
        String msg = JSON.toJSONString(map);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video, msg);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //合并文件
    private File mergeFile(List<File> chunkFileList, File mergeFile) {
        try {
            //如果合并文件已存在则删除，否则创建新文件
            if (mergeFile.exists()) {
                mergeFile.delete();
            } else {
                mergeFile.createNewFile();
            }
            //对快文件进行排序
            Collections.sort(chunkFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                        return 1;
                    }
                    return -1;
                }
            });
            //创建一个写对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile,"rw");
            byte[] b = new byte[1024];
            for (File chunkFile : chunkFileList) {
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
                int len;
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b,0,len);
                }
                raf_read.close();
            }
            raf_write.close();
            return mergeFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //检验md5值
    private boolean checkFileMd5(File mergeFile, String fileMd5) {
        try {
            //创建文件输入流
            FileInputStream inputStream = new FileInputStream(mergeFile);
            //得到文件md5
            String md5Hex = DigestUtils.md5Hex(inputStream);
            //和传入的md5比较
            if (fileMd5.equalsIgnoreCase(md5Hex)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}