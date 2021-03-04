package com.enjoy.jack.bean;

import lombok.Data;

/**
 * @Classname Student
 * @Description TODO
 * @Author Jack
 * Date 2020/12/7 15:41
 * Version 1.0
 */
@Data
public class Student {
    private String username = "jack";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
