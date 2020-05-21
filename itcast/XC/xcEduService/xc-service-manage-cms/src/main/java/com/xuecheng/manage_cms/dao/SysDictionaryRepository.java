package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.Repository;

public interface SysDictionaryRepository extends MongoRepository<SysDictionary,String> {
    SysDictionary findByDType(String type);
}
