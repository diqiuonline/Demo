package com.dhcc;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/2 14:18
 */
public class log4jTest {
    //定义log4j日志记录器
    public static final Logger LOGGER = Logger.getLogger(log4jTest.class);
    //测试桥接器
    @Test
    public void testQuick() throws Exception {
        LOGGER.info("hello log4j");
    }
}
