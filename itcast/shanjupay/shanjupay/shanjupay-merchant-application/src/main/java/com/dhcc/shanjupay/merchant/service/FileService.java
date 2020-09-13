package com.dhcc.shanjupay.merchant.service;

import com.dhcc.shanjupay.common.domain.BusinessException;
import org.springframework.stereotype.Service;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/16 17:40
 */
public interface FileService {
    /**
     * 上传文件
     * @param bytes 文件字节数据
     * @param fileName 文件名
     * @return 文件访问路径 （绝对url地址）
     * @throws BusinessException
     */
    String upload(byte[] bytes, String fileName) throws BusinessException;

}
