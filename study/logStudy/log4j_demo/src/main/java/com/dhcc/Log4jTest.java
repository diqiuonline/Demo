package com.dhcc;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;
import org.junit.Test;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/1 16:44
 */
public class Log4jTest {
    @Test
    public void testQuick() throws Exception {
        //开启log4j内置日志记录
        //LogLog.setInternalDebugging(true);
        //初始化配置信息 在入门案例中 暂不使用配置文件
        //BasicConfigurator.configure();

        //获取日志记录器对象
        Logger logger = Logger.getLogger(Log4jTest.class);
        //日志记录输出
        //logger.info("hello log4j");
        //日志级别
        for (int i = 0; i < 100; i++) {
            logger.fatal("fatal"); //严重错误 一般会造成系统崩溃并终止运行
            logger.error("error"); //错误信息 不会影响系统运行
            logger.warn("warn"); //警告信息，可能会发生问题
            logger.info("info"); // 系统运行信息 数据库连接 网络连接 io操作
            logger.debug("debug"); //调试信息   一般在开发中使用 记录程序变量参数传递信息   //默认级别
            logger.trace("trace"); //追踪信息  记录程序所有的流程信息~
        }
    }
}

