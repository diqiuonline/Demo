package com.dhcc.mp.simple;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.dhcc.mp.simple.dao.UserMapper;
import com.dhcc.mp.simple.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/7 15:30
 */

public class UserTestPlus {

    public static final Logger log = LoggerFactory.getLogger(UserTestPlus.class);

    @Test
    public void demoQuick() throws Exception {
        String config = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(config);
        SqlSessionFactory sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        /*List<User> all = mapper.findAll();*/
        List<User> all = mapper.selectList(null);
        for (User user : all) {
            log.info(String.valueOf(user));
        }

    }
}