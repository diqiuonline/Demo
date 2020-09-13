package com.dhcc.mp.simple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhcc.mp.simple.emuns.SexEnum;
import com.dhcc.mp.simple.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    @Rollback(false)
    public void SelectById() throws Exception {
        User user = new User();
        user.setId(8L);
        User user1 = user.selectById();
        log.info(user1.toString());
    }
    @Test
    public void InsertTest() throws Exception {
        User user = new User(null, "lijinzhuo", "1", "李锦卓", 35, "453@daf.com");
        boolean insert = user.insert();
        log.info(user.toString()+"======"+insert);
    }
    @Test
    public void updateTest() throws Exception {
        User user = new User();
        user.setId(3L);
        user.setUsername("犬夜叉23452");
        boolean b = user.updateById();
        log.info(user.toString()+"============="+b);
    }
    @Test
    public void deleteTest() throws Exception {
        User user = new User();
        boolean b = user.deleteById(8L);
        log.info(String.valueOf(b));
    }
    @Test
    public void TestSelect() throws Exception {
        User user = new User();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.ge("age", 30);  //大于等于30岁的用户
        List<User> users = user.selectList(wrapper);
        for (User user1 : users) {
            log.info(user1.toString());
        }
    }

    @Test
    public void AllUpdate() throws Exception {
        User user = new User();
        user.setUsername("皇帝");

        boolean b = user.update(null);
        log.info(user.toString()+"============="+b);
    }
    /*@Test
    public void testLockUpdateVersion() {
        User user = new User();
        user.setId(3L);
        user.setPassword("qweewtr");
        user.setVersion(1); //当前的版本信息
        boolean b = user.updateById();
        log.info(user.toString()+"============="+b);
    }*/
    @Test
    public void testSelectBySex() throws Exception {
        User user = new User();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("sex", SexEnum.MAN);
        List<User> users = user.selectList(wrapper);
        for (User user1 : users) {
            log.info(user1.toString());
        }
    }

}
