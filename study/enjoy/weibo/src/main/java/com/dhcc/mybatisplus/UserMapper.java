package com.dhcc.mybatisplus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("{sql}")
    void insertPojo(@Param("sql") String sql);


    @Insert("INSERT INTO facebook1_copy#{tableNum} (id, html) VALUES (#{id}, #{html})")
    int insertUser(@Param("tableNum") int tableNum, @Param("id") int id, @Param("html") String html);
}
