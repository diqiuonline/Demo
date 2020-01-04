package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/26 19:12
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime> {
    //属性驱动
    @Autowired
    private TakeTimeService takeTimeService;

    @Action(value = "taketime_findAll")
    public void findAll() throws IOException {
        List<TakeTime>list = takeTimeService.findAll();
        ListToJson(list);
    }
}