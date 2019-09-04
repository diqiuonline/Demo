package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2019/3/7 15:23
 */
@Api(value = "课程分类管理", description = "课程分类管理")
public interface CategoryControllerApi {
    @ApiOperation("课程分类查询")
    CategoryNode findList();
}