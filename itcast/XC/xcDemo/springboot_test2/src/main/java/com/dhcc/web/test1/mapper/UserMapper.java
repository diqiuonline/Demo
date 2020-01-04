package com.dhcc.web.test1.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    @Insert("insert into user (username,password)values(#{username},#{password})")
    int insert(@Param("username") String username, @Param("password") String password);

}
