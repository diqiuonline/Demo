package com.dhcc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/1 21:23
 */
public class JclTest {
    @Test
    public void testQuick() throws Exception {
        //获取log日志记录器
        Log log = LogFactory.getLog(JclTest.class);
        //获取日志记录的输出
        log.info("hello jcl");
    }
}
