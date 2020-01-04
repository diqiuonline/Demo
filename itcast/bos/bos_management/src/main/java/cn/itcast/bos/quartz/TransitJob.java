package cn.itcast.bos.quartz;

import cn.itcast.bos.service.transit.TransitInfoService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: 李锦卓
 * Time: 2018/9/12 20:49
 */
public class TransitJob implements Job {
    @Autowired
    private TransitInfoService transitInfoService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("更行数据库-----------");
        transitInfoService.synxIndex();
    }
}