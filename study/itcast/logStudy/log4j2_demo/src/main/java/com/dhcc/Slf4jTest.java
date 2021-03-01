package com.dhcc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/2 13:37
 */
public class Slf4jTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(Slf4jTest.class);
    //快速入门
    @Test
    public void testQuick() throws Exception {
        LOGGER.error("error");
        LOGGER.warn("warn");
        LOGGER.info("info"); //默认基本
        LOGGER.debug("debug");
        LOGGER.trace("trace");


    }
}
