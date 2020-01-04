package com.dhcc.web.controller;

import com.dhcc.web.test1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User2: 李锦卓
 * Time: 2019/2/18 11:46
 */
@Controller
@RequestMapping("/user")
public class UserController {
  /*  @RequestMapping("{id}")
    public User2 getUser(@PathVariable Integer id) {
        User2 user = new User2("李锦卓", "123");
        user.setId(id);
        int i = 1/0;
        return user;
    }*/

 /*   @Autowired
    private StudentService studentService;

    @RequestMapping("/add")
    public String add(String uid,String username,String password){
        studentService.add(uid,username, password);
        return "success";
    }*/
/*    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/save")
    public String save(String username, String password) {
        userMapper.save(username, password);
        return "success";
    }

    @RequestMapping("/get")
    public User get(String username){
        User user = userMapper.get(username);
        return user;
    }*/
    @Autowired
    private UserService userService;

    @RequestMapping("/and")
    public String add(String name) {
        userService.save(name,"342");
        return "user/user";
    }
}