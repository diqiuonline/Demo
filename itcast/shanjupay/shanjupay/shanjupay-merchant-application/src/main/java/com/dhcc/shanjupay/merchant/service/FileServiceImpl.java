package com.dhcc.shanjupay.merchant.service;

import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import com.dhcc.shanjupay.common.domain.ErrorCode;
import com.dhcc.shanjupay.common.util.QiniuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/16 17:44
 */
@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(QiniuUtils.class);
    @Value("${oss.qiniu.url}")
    private String url;

    @Value("${oss.qiniu.accessKey}")
    private String accessKey;

    @Value("${oss.qiniu.secretKey}")
    private String secretKey;

    @Value("${oss.qiniu.bucket}")
    private String bucket;

    /**
     * @param bytes    文件字节数据
     * @param fileName 文件名
     * @return
     * @throws BusinessException
     */
    @Override
    public String upload(byte[] bytes, String fileName) throws BusinessException {

        //调用common下面的工具类
        // String accessKey, String secretKey, String bucket, byte[] uploadBytes, String key) throws RuntimeException{
        try {
            QiniuUtils.upload2qiniu(accessKey, secretKey, bucket, bytes, fileName);
        } catch (Exception e) {
            log.error(String.valueOf(CommonErrorCode.E_100106.getCode())+":"+CommonErrorCode.E_100106.getDesc());
            throw new BusinessException(CommonErrorCode.E_100106);

        }
        return url + fileName;
    }
}
