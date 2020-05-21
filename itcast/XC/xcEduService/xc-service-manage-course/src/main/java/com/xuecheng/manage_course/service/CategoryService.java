package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: 李锦卓
 * Time: 2019/3/19 11:08
 */
@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryNode findList() {
        return categoryMapper.findList();
    }
}