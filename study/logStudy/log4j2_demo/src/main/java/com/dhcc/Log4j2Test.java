package com.dhcc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/3 23:42
 */
public class Log4j2Test {
    public static final Logger LOGGER = LogManager.getLogger(Log4j2Test.class);
    @Test
    public void log4j2Quick() throws Exception {
        //快速入门
        //日志记录的输出
        for (int i = 0; i < 2; i++) {

            LOGGER.fatal("fatal"); //严重错误 一般会造成系统崩溃并终止运行
            LOGGER.error("error"); //错误信息 不会影响系统运行
            LOGGER.warn("warn"); //警告信息，可能会发生问题
            LOGGER.info("info"); // 系统运行信息 数据库连接 网络连接 io操作
            LOGGER.debug("debug"); //调试信息   一般在开发中使用 记录程序变量参数传递信息   //默认级别
            LOGGER.trace("trace"); //追踪信息  记录程序所有的流程信息~
        }
    }
}
