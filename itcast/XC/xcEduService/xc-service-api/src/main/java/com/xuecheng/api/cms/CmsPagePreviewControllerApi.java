package com.xuecheng.api.cms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "页面预览接口",description = "提供页面的预览")
public interface CmsPagePreviewControllerApi {
    @ApiOperation("提供页面的预览")
    void preview(String pageId);
}
