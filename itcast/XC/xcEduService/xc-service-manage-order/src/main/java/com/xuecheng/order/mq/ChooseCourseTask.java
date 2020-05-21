package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2019/5/28 12:16
 */
@Component
public class ChooseCourseTask {
    @Autowired
    TaskService taskService;

    //每隔一分钟扫描表，想mq发送信息
    @Scheduled(cron="0/3 * * * * *")
    public void sendChoosecourseTask() {
        //取出当前一分钟前的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE, -1);
        Date time = calendar.getTime();
        List<XcTask> xcTaskList = taskService.findXcTaskList(time, 1000);
        for (XcTask xcTask : xcTaskList) {
            //取任务
            if (taskService.getTask(xcTask.getId(), xcTask.getVersion()) > 0) {
                String ex = xcTask.getMqExchange();
                String routingkey = xcTask.getMqRoutingkey();
                taskService.publish(xcTask, ex, routingkey);
            }

        }

    }

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void receiveFinishChoosecourseTask(XcTask xcTask){
        if(xcTask!=null && StringUtils.isNotEmpty(xcTask.getId())){
            taskService.finishTask(xcTask.getId());
        }
    }
}