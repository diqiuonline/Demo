package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * User: 李锦卓
 * Time: 2019/3/5 10:49
 */
@Service
@Transactional
public class CourseService {
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private TeachplanRepository teachplanRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private CoursePubRepository coursePubRepository;
    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    private TeachplanMediaPubRepository teachplanMediaPubRepository;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    //查询课程计划
    public TeachplanNode findTeacherpplanList(String courseId) {
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }

    //查询我的课程
    public QueryResponseResult<CourseInfo> findCourseBaseList(String companyId, int page, int size,
                                                              CourseListRequest courseListRequest) {

        //页码
        if (page <= 0) {
            page = 1;
        }
        // page = page - 1;
        if (size <= 0) {
            size = 20;
        }
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        courseListRequest.setCompanyId(companyId);

        //分页对象
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setTotal(courseListPage.getTotal());
        queryResult.setList(courseListPage.getResult());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }
    //添加课程计划
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid())
                || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //取出课程id
        String courseid = teachplan.getCourseid();
        //取出父节点id
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            parentid = this.getTeachplanRoot(courseid);
        }
        //查询根节点id
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan teachplan1 = optional.get();
        //父节点的级别
        String parent_grade = teachplan1.getGrade();
        //创建一个新节点准备添加
        Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan,teachplanNew);
        //要设置必要的属性
        teachplanNew.setParentid(parentid);
        if(parent_grade.equals("1")){
            teachplanNew.setGrade("2");
        }else{
            teachplanNew.setGrade("3");
        }
        teachplanNew.setStatus("0");//未发布
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);

    }
    //获取当前课程的根节点
    private String getTeachplanRoot(String courseId){
        //校验课程id
        Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
        if (!byId.isPresent()) {
            return null;
        }
        CourseBase courseBase = byId.get();
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() == 0) {
            //新增一个跟节点
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseId);
            teachplan.setParentid("0");
            teachplan.setGrade("1");//一级结点
            teachplan.setStatus("0");
            teachplan.setPname(courseBase.getName());
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        return teachplanList.get(0).getId();

    }
    //获取课程基本信息
    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
        if (byId.isPresent()) {
            CourseBase courseBase = byId.get();
            return courseBase;
        }
        return null;
    }
    //修改课程信息
    public ResponseResult updataCourseBase(String courseId, CourseBase courseBase) {
        CourseBase courseBaseById = this.getCourseBaseById(courseId);
        if (courseBaseById == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        courseBase.setId(courseId);
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //查询课程营销信息
    public CourseMarket getCourseMarketById(String courseId){
        Optional<CourseMarket> byId = courseMarketRepository.findById(courseId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }
    //更新课程营销信息
    public ResponseResult updateCourseMarket(String courseId,CourseMarket courseMarket){
        courseMarket.setId(courseId);
        courseMarketRepository.save(courseMarket);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //添加课程图片
    public ResponseResult saveCoursePic(String courseId,String pic){
        //查询课程图片
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (optional.isPresent()) {
            coursePic = optional.get();
        } else {
            //没有课程图片
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //查询课程图片
    public CoursePic findCoursePic(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
    //添加课程基本信息
    public ResponseResult addCourseBase(CourseBase courseBase){
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //删除图片
    public ResponseResult deleteCoursePic(String courseId) {
        Long aLong = coursePicRepository.deleteByCourseid(courseId);
        if (aLong > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
    //课程视图查询
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        //查询课程基本信息
        CourseBase courseBase = this.getCourseBaseById(id);
        courseView.setCourseBase(courseBase);
        //查询课程营销信息
        CourseMarket courseMarket = this.getCourseMarketById(id);
        courseView.setCourseMarket(courseMarket);
        //查询课程图片信心
        CoursePic coursePic = this.findCoursePic(id);
        courseView.setCoursePic(coursePic);
        //查询课程计划
        TeachplanNode teachplanNode = this.findTeacherpplanList(id);
        courseView.setTeachplanNode(teachplanNode);
        //查询课程媒资信息--错误的尝试
      /*  List<TeachplanNode> list = teachplanNode.getChildren();
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (TeachplanNode firstChildren : list) {
            List<TeachplanNode> children = firstChildren.getChildren();
            for (TeachplanNode secordChildren : children) {
                Optional<TeachplanMediaPub> byId = teachplanMediaPubRepository.findById(secordChildren.getId());
                if (byId.isPresent()) {
                    teachplanMediaPubList.add(byId.get());
                }
            }
        }
        courseView.setTeachplanMediaPubList(teachplanMediaPubList);*/
        return courseView;
    }
    //课程预览
    public CoursePublishResult preview(String id){
        //查询课程
        CourseBase courseBase = this.getCourseBaseById(id);
        //请求cms添加页面
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//数据模型url
        cmsPage.setPageName(id+".html");//页面名称
        cmsPage.setPageAliase(courseBase.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id
        //远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面预览url
        String url = previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS,url);
    }

    //课程发布
    public CoursePublishResult publish(String id) {
        //查询课程
        CourseBase courseBaseById = this.getCourseBaseById(id);
        //准备页面信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//数据模型url
        cmsPage.setPageName(id+".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id
        //调用cms一键发布接口将课程详情页面发布到服务器
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //保存课程的状态为已发布
        CourseBase courseBase = this.saveCoursePubState(id);
        if (courseBase == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //保存课程索引信息
        //先创建一个coursepub对象
        CoursePub coursePub = this.createCoursePub(id);
        //将coursepub存储到数据库
        this.saveCoursePub(id, coursePub);

        //缓存课程的信息
        //...
        //得到页面的url
        String url = cmsPostPageResult.getPageUrl();
        this.saveTeachplanMediaPub(id);
        return new CoursePublishResult(CommonCode.SUCCESS, url);
    }
    //向teachplanMediaPub中保存课程媒资信息
    private void saveTeachplanMediaPub(String id) {
        //先删除teachplanMediaPub中的数据
        teachplanMediaPubRepository.deleteByCourseId(id);
        //从teachplanMedia中查询
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(id);
        List<TeachplanMediaPub> newlist = new ArrayList<>();
        //将teachplanMediaList中的数据存入到newlist中
        for (TeachplanMedia teachplanMedia : teachplanMediaList) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia, teachplanMediaPub);
            //添加时间戳
            teachplanMediaPub.setTimestamp(new Date());
            newlist.add(teachplanMediaPub);
        }
        //将newlist存入到teachplanmediapub
        teachplanMediaPubRepository.saveAll(newlist);

    }

    //更新课程状态为已发布
    private CourseBase saveCoursePubState(String id) {
        CourseBase courseBase = this.getCourseBaseById(id);
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }

    //保存coursePub
    private CoursePub saveCoursePub(String id, CoursePub coursePub) {
        CoursePub coursePubNew = null;
        //根据课程id查询coursepub
        Optional<CoursePub> byId = coursePubRepository.findById(id);
        if (byId.isPresent()) {
            coursePubNew = byId.get();
        } else {
            coursePubNew = new CoursePub();
        }
        //将coursepub中的信息保存到coursepubnew中
        BeanUtils.copyProperties(coursePub, coursePubNew);
        coursePubNew.setId(id);
        //时间戳，给logstach使用
        coursePubNew.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String data = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(data);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }

    //创建coursePub对象
    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        //根据课程id查询coursePub
        Optional<CourseBase> byId = courseBaseRepository.findById(id);
        if (byId.isPresent()) {
            BeanUtils.copyProperties(byId.get(),coursePub);
        }
        //查询课程图片
        CoursePic coursePic = this.findCoursePic(id);
        BeanUtils.copyProperties(coursePic,coursePub);
        //查询课程营销信息
        CourseMarket courseMarket = this.getCourseMarketById(id);
        BeanUtils.copyProperties(courseMarket, coursePub);
        //查询课程计划信息
        TeachplanNode teachplanNode = this.findTeacherpplanList(id);
        String s = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(s);
        return coursePub;
    }

    //保存媒资信息
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //检查课程计划是否时三级
        Optional<Teachplan> byId = teachplanRepository.findById(teachplanMedia.getTeachplanId());
        if (!byId.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Teachplan teachplan = byId.get();
        String grade = teachplan.getGrade();
        if (StringUtils.isEmpty(grade) || !"3".equals(grade)) {
            //只允许3级的课程计划
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        //查询teachplanmedia
        Optional<TeachplanMedia> byId1 = teachplanMediaRepository.findById(teachplanMedia.getTeachplanId());
        TeachplanMedia one = null;
        if (byId1.isPresent()) {
            one = byId1.get();
        } else {
            one = new TeachplanMedia();
        }
        //存入信息
        one.setCourseId(teachplan.getCourseid());//课程id
        one.setMediaId(teachplanMedia.getMediaId());//媒资文件的id
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());//媒资文件的原始名称
        one.setMediaUrl(teachplanMedia.getMediaUrl());//媒资文件的url
        one.setTeachplanId(teachplanMedia.getTeachplanId());
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

}