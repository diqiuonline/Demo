package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * User: 李锦卓
 * Time: 2019/5/5 17:05
 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {
    @Autowired
    private MediaUploadService mediaUploadService;
    //文件上传前注册
    @Override
    @PostMapping("/register")
    public ResponseResult register(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName") String fileName,
                                   @RequestParam("fileSize") Long fileSize, @RequestParam("mimetype") String mimetype,
                                   @RequestParam("fileExt") String fileExt) {
        return mediaUploadService.regist(fileMd5, fileName, fileSize, mimetype, fileExt);
    }
    //分块检查
    @Override
    @PostMapping("/checkchunk")
    public CheckChunkResult checkchunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") Integer chunk) {
        return mediaUploadService.checkchunk(fileMd5,chunk);
    }
    //上传分块
    @Override
    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunk(@RequestParam("file") MultipartFile file, @RequestParam("fileMd5") String fileMd5,
                                      @RequestParam("chunk") Integer chunk) {
        return mediaUploadService.uploadchunk(file, fileMd5, chunk);
    }
    //合并分块
    @Override
    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName") String fileName,
                                      @RequestParam("fileSize") Long fileSize, @RequestParam("mimetype") String mimetype,
                                      @RequestParam("fileExt") String fileExt) {
        return mediaUploadService.mergechunks(fileMd5, fileName, fileSize, mimetype, fileExt);
    }
}