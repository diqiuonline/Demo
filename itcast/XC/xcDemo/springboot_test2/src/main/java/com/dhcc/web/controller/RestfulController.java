package com.dhcc.web.controller;

import com.bjucloud.open.sdk.model.DataBean;
import com.dhcc.web.domain.User;
import org.springframework.web.bind.annotation.*;

/**
 * User: 李锦卓
 * Time: 2019/3/24 15:39
 */
@RestController
public class RestfulController {
    @RequestMapping("/list")
    public DataBean getString(@RequestBody User user){
        DataBean dataBean = new DataBean();
        dataBean.setDatas(user);
        dataBean.setSuccess(true);
        return dataBean;
    }

    @RequestMapping("/add")
    public DataBean getString2(String username){
        DataBean dataBean = new DataBean();
        dataBean.setDatas(username);
        dataBean.setSuccess(false);
        return dataBean;
    }
}