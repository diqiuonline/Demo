package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * User: 李锦卓
 * Time: 2019/2/28 20:29
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
}