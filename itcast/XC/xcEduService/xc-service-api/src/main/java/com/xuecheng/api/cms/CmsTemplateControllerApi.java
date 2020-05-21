package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * User: 李锦卓
 * Time: 2019/1/7 21:09
 */
@Api(value = "cms模板管理接口", description = "cms模板管理接口，提供模板的查询")
public interface CmsTemplateControllerApi {
    @ApiOperation("模板查询页面列表")
    QueryResponseResult findAll();
}