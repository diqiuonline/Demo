package com.dhcc.web.domain;

/**
 * User2: 李锦卓
 * Time: 2019/2/18 11:45
 */
public class User2 {
    private Integer id;
    private String username;
    private String password;

    public User2() {
    }

    public User2(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User2(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}