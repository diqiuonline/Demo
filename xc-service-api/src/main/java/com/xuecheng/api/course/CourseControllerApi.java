package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * User: 李锦卓
 * Time: 2019/3/4 15:17
 */
@Api(value = "course课程管理接口",description = "course课程管理接口 提供课程的增删改查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询课程基本信息")
    CourseBase getCourseBaseById(String courseId);

    @ApiOperation("修改课程信息")
    ResponseResult updataCourseBase(String courseId,CourseBase courseBase);

    @ApiOperation("查询课程营销")
    CourseMarket getCourseMarkerById(String courseId);

    @ApiOperation("更新课程营销信息")
    ResponseResult updateCourseMarket(String courseId,CourseMarket courseMarket);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePic(String courseId,String pic);

    @ApiOperation("图片查询")
    CoursePic findCoursePic(String courseId);

    @ApiOperation("添加课程基本信息")
    ResponseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("删除图片")
    ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    CourseView courseview(String id);

    @ApiOperation("课程预览")
    CoursePublishResult preview(String id);

    @ApiOperation("课程发布")
    CoursePublishResult publish(String id);

    @ApiOperation("保存媒资信息")
    ResponseResult saveMedia(TeachplanMedia teachplanMedia);
}