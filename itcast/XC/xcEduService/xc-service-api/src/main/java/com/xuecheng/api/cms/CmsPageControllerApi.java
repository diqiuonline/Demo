package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description="cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    /**
     * 页面查询
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value="每页记录数",required=true,paramType="path",dataType="int")
            })
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 页面添加
     * @param cmsPage
     * @return
     */
    @ApiOperation("页面添加")
    CmsPageResult add(CmsPage cmsPage);

    /**
     * 通过id查询页面
     * @param id
     * @return
     */
    @ApiOperation("通过id查询页面")
    CmsPage findById(String id);

    /**
     * 修改页面
     * @param id
     * @param cmsPage
     * @return
     */
    @ApiOperation("修改页面")
    CmsPageResult edit(String id,CmsPage cmsPage);

    /**
     * 修改页面
     * @param id
     * @return
     */
    @ApiOperation("删除页面")
    ResponseResult delete(String id);

    /**
     * 页面发布
     * @param pageId
     * @return
     */
    @ApiOperation("CMS页面发布")
    ResponseResult post(String pageId);

    /**
     * 页面预览
     * @param cmsPage
     * @return
     */
    @ApiOperation("保存页面")
    CmsPageResult save(CmsPage cmsPage);

    /**
     * 一键发布
     * @param cmsPage
     * @return
     */
    @ApiOperation("Course一键发布页面")
    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
