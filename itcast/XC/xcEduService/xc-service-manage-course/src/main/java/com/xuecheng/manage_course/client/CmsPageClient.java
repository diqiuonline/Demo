package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.manage_course.client.impl.CmsPageClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "XC-SERVICE-MANAGE-CMS",fallback = CmsPageClientImpl.class)  //指定远程调用的服务名 和 熔断器
public interface CmsPageClient {
    //根据页面id查询页面信息 远程调用cms接口请求数据
    @GetMapping("/cms/page/get/{id}")
    CmsPage findCmsPageById(@PathVariable("id") String id);
    //根据页面信息更新页面
    @PostMapping("/cms/page/save")
    CmsPageResult save(@RequestBody CmsPage cmsPage);
    //发布页面
    @PostMapping("/cms/page/postPageQuick")
    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
