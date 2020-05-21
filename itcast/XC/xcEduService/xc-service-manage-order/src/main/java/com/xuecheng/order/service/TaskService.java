package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * User: 李锦卓
 * Time: 2019/5/28 12:12
 */
@Service
public class TaskService {
    @Autowired
    XcTaskRepository xcTaskRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    //查询前n条任务
    public List<XcTask> findXcTaskList(Date updateTime, int size) {
        //设置分页参数
        Pageable pageable = new PageRequest(0, size);
        //查询前n条任务
        Page<XcTask> all = xcTaskRepository.findByUpdateTimeBefore(pageable, updateTime);
        List<XcTask> xcTaskList = all.getContent();
        return xcTaskList;
    }

    //发布消息
    public void publish(XcTask xcTask, String ex, String routeKey) {
        Optional<XcTask> byId = xcTaskRepository.findById(xcTask.getId());
        if (byId.isPresent()) {
            rabbitTemplate.convertAndSend(ex, routeKey, xcTask);
            //更新任务时间
            XcTask one = byId.get();
            one.setUpdateTime(new Date());
            xcTaskRepository.save(one);
        }
    }

    //获取任务
    @Transactional
    public int getTask(String id, int version) {
        int i = xcTaskRepository.updateTaskVersion(id, version);
        return i;
    }

    //完成任务
    @Transactional
    public void finishTask(String xcTaskId) {
        Optional<XcTask> optionalXcTask = xcTaskRepository.findById(xcTaskId);
        if (optionalXcTask.isPresent()) {
            //当前任务
            XcTask xcTask = optionalXcTask.get();
            //历史任务
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
            xcTaskRepository.delete(xcTask);
        }
    }
}