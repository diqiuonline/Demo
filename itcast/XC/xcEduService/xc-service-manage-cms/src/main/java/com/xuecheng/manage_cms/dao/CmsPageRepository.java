package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    CmsPage findByPageName(String pageName);

    /**
     * 更具页面名称，站点id，页面webpath查询页面
     * @param pageName
     * @param siteId
     * @param pageWebPath
     * @return
     */
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
