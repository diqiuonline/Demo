package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: 李锦卓
 * Time: 2019/3/19 11:27
 */
@Service
public class SysDictionaryService {
    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary findByType(String type) {
        return sysDictionaryRepository.findByDType(type);
    }
}