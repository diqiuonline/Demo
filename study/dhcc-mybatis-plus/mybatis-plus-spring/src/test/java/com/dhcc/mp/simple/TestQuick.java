package com.dhcc.mp.simple;

import com.dhcc.mp.simple.dao.UserMapper;
import com.dhcc.mp.simple.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/7 16:58
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestQuick {
    public static final Logger log = LoggerFactory.getLogger(TestQuick.class);
    @Autowired
    private UserMapper userMapper;
    @Test
    public void demoQuick() throws Exception {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            log.info(String.valueOf(user));

        }
    }
}
