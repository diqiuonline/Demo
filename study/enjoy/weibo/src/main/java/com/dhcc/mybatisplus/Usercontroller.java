package com.dhcc.mybatisplus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class Usercontroller {
    @Autowired
    IUserService userService;


    public void save(User user) {
        userService.save(user);
    }
}
