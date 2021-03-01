package com.dhcc.mvc;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/16 20:56
 */
@RestController
public class DhccController {
    @RequestMapping("/dhcc")
    public String dhcc() {
        System.out.println("dhcc is nice controller");
        return "index";
    }
}
