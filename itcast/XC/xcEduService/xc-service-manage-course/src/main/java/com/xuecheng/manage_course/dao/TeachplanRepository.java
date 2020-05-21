package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
    //定义方法更具课程idhe和父节点查询列表
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
