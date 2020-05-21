package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.SysDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: 李锦卓
 * Time: 2019/3/19 11:25
 */
@RestController
@RequestMapping("/sys")
public class SysDicthinaryController implements SysDicthinaryControllerApi {
    @Autowired
    private SysDictionaryService sysDictionaryService;
    @Override
    @GetMapping("/dictionary/get/{dType}")
    public SysDictionary getByType(@PathVariable("dType") String type) {
        return sysDictionaryService.findByType(type);
    }
}