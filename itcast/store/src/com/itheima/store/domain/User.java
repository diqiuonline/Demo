package com.itheima.store.domain;

import java.util.Date;

/**
 * /**
 *  * 用户模块的实体类：
 *  * @author admin
 *  *CREATE TABLE `user` (
 *   `uid` varchar(32) NOT NULL,
 *   `username` varchar(20) DEFAULT NULL,
 *   `password` varchar(20) DEFAULT NULL,
 *   `name` varchar(20) DEFAULT NULL,
 *   `email` varchar(30) DEFAULT NULL,
 *   `telephone` varchar(20) DEFAULT NULL,
 *   `birthday` date DEFAULT NULL,
 *   `sex` varchar(10) DEFAULT NULL,
 *   `state` int(11) DEFAULT NULL,
 *   `code` varchar(64) DEFAULT NULL,
 *   PRIMARY KEY (`uid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 *  */
public class User {
    private String uid;
    private String username;
    private String password;
    private String name;
    private String email;
    private String telephone;
    private Date birthday;
    private String sex;
    private Integer state;
    private String code;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
