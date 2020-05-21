package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2019/4/10 23:07
 */
@Data
@ToString
@NoArgsConstructor
public class CourseView implements Serializable {
    private CourseBase courseBase; //基础信息
    private CourseMarket courseMarket; //课程营销信息
    private CoursePic coursePic;  //课程图片
    private TeachplanNode teachplanNode; //课程计划
    //private List<TeachplanMediaPub> teachplanMediaPubList; //课程媒资计划
}