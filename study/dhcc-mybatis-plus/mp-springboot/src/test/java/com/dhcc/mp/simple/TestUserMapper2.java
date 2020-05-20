package com.dhcc.mp.simple;

import com.dhcc.mp.simple.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/9 13:36
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUserMapper2 {
    public static final Logger log = LoggerFactory.getLogger(TestUserMapper2.class);
    @Test
    public void SelectById() throws Exception {
        User user = new User();
        user.setId(2L);
        User user1 = user.selectById();
        log.info(user1.toString());
    }
    @Test
    public void InsertTest() throws Exception {
        User user = new User(null, "lijinzhuo", "abcde", "李锦卓", 35, "453@daf.com");
        boolean insert = user.insert();
        log.info(user.toString()+"======"+insert);
    }
    @Test
    public void updateTest() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setUsername("犬夜叉");
        boolean b = user.updateById();
        log.info(user.toString()+"============="+b);
    }
}
