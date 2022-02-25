package com.dhcc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 李锦卓
 * 2022/2/19 12:44
 * 1.0
 */
@Controller
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("/list")
    public String toList() {
        return "list";
    }
}
