package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2019/3/7 15:26
 */
@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list")
    @Override
    public CategoryNode findList() {
        CategoryNode list = categoryService.findList();
        return list;
    }
}