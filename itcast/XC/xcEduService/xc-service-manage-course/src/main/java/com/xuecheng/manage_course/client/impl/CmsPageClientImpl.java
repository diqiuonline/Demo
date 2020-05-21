package com.xuecheng.manage_course.client.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.springframework.stereotype.Component;

/**
 * User: 李锦卓
 * Time: 2019/9/16 23:25
 */
@Component
public class CmsPageClientImpl implements CmsPageClient {
    @Override
    public CmsPage findCmsPageById(String id) {
        return new CmsPage();
    }

    @Override
    public CmsPageResult save(CmsPage cmsPage) {
        return new CmsPageResult(CmsCode.CMS_HYSTRIX, null);
    }

    @Override
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        return new CmsPostPageResult(CmsCode.CMS_HYSTRIX,null);
    }
}