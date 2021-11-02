package com.roncoo.eshop.inventory.controller;

import com.roncoo.eshop.inventory.model.User;
import com.roncoo.eshop.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo() {

        return userService.findUserInfo();

    }


    @RequestMapping("/getCacheUserInfo")
    @ResponseBody
    public User getCacheUserInfo() {

        return userService.getCacheUserInfo();

    }
}
