package cn.dhcc.pinyougou.test.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * 李锦卓
 * 2022/3/19 13:27
 * 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class SeckillGoodsToRedisTest {
    @Test
    public void testTask() throws IOException {
        while (true) {
            System.in.read();
        }
    }
}
