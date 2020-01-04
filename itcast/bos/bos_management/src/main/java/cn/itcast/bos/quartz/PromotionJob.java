package cn.itcast.bos.quartz;

import cn.itcast.bos.service.take_delivery.PromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * User: 李锦卓
 * Time: 2018/9/2 16:03
 */
public class PromotionJob implements Job {
    @Autowired
    private PromotionService promotionService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("活动过期处理程序");
        System.out.println(123);
        //SimpleDateFormat.
        //每分钟执行一次当前时间大于promotion 中的endDate 活动已经过期 设置status = 2
        promotionService.updateStatus(new Date());
    }
}