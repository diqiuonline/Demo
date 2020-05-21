package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * User: 李锦卓
 * Time: 2019/1/7 21:15
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {
}