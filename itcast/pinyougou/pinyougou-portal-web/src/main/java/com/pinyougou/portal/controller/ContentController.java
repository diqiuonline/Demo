package com.pinyougou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/10/18 11:43
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;
    /**
     * 根据广告分类id查询广告列表
     */
    @RequestMapping("/findByCategoryId")
    public List<TbContent>findByCategoryId(Long categoryId){
        List<TbContent> contents = contentService.findByCategoryId(categoryId);
        return contents;
    }
}