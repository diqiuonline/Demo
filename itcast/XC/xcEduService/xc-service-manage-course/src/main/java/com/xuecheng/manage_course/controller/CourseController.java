package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * User: 李锦卓
 * Time: 2019/3/5 10:52
 */
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {
    @Autowired
    private CourseService courseService;

    //查询课程计划
    @PreAuthorize("hasAuthority('course_teachplan_list')")
    @GetMapping("/teachplan/list/{courseId}")
    @Override
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String id) {
        TeachplanNode teacherpplanList = courseService.findTeacherpplanList(id);
        return teacherpplanList;
    }

    /**
     * 分页查询我的课程
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/coursebase/list/{page}/{size}")
    @PreAuthorize("hasAuthority('course_find_list')")
    @Override
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page, @PathVariable("size") int size,
                                                          CourseListRequest courseListRequest) {
        //获取当前用户信息
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        //当前用户所属单位id
        String companyId = userJwt.getCompanyId();
        return courseService.findCourseBaseList(companyId,page,size,courseListRequest);
    }
    //添加课程计划
    @PostMapping("/teachplan/add")
    @Override
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }
    //查询课程
    @Override
    @GetMapping("/coursebase/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable String courseId) {
        CourseBase courseBaseById = courseService.getCourseBaseById(courseId);
        return courseBaseById;
    }
    //修改课程
    @Override
    @PutMapping("/updatecourse/{courseId}")
    public ResponseResult updataCourseBase(@PathVariable String courseId, @RequestBody CourseBase courseBase) {
        return courseService.updataCourseBase(courseId,courseBase);
    }
    //查询课程营销
    @Override
    @GetMapping("/getcoursemarket/{courseId}")
    public CourseMarket getCourseMarkerById(@PathVariable String courseId) {
        return courseService.getCourseMarketById(courseId);
    }
    //更新课程营销信息
    @Override
    @PostMapping("/updaatecoursemarket/{courseId}")
    public ResponseResult updateCourseMarket(@PathVariable String courseId, @RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(courseId,courseMarket);
    }

    /**
     * 添加课程图片
     * @param courseId
     * @param pic
     * @return
     */
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam String courseId, @RequestParam String pic) {
        ResponseResult responseResult = courseService.saveCoursePic(courseId, pic);
        return responseResult;
    }

    /**
     * 查询图片
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable String courseId) {
        return courseService.findCoursePic(courseId);
    }

    /**
     * 添加课程基本信息
     * @param courseBase
     * @return
     */
    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    /**
     * 删除图片
     * @param courseId
     * @return
     */
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    /**
     * 课程视图查询
     * @param id
     * @return
     */
    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    /**
     * 课程预览
     * @param id
     * @return
     */
    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    /**
     * 课程发布
     * @param id
     * @return
     */
    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String id) {
        return courseService.publish(id);
    }
    //保存媒资信息
    @Override
    @PostMapping("/savemedia")
    public ResponseResult saveMedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.saveMedia(teachplanMedia);
    }


}