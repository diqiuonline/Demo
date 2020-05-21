package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPagePreviewControllerApi;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: 李锦卓
 * Time: 2019/1/14 22:35
 */
@Controller
@RequestMapping("/cms")
public class CmsPagePreviewController extends BaseController implements CmsPagePreviewControllerApi {
    @Autowired
    private PageService pageService;

    @Value("${cms.ip}")
    private String ip;

    @Override
    @GetMapping("/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) {
        System.out.println(ip);
        pageService.preview(pageId, response);
    }

}
