package com.dhcc;

import org.junit.Test;

import java.io.InputStream;
import java.util.logging.*;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/30 19:47
 */
public class JULTest {
    //快速入门
    @Test
    public void testQuink() throws Exception {
        //获取日志记录器对象
        Logger logger = Logger.getLogger("com.dhcc.JULTest");
        //日志记录输出
        logger.info("hello jul");
        //通用方法进行日志记录
        logger.log(Level.INFO, "infomessage");
        //通过占位符的方式 输出变量值
        String name = "itcsast";
        Integer age = 13;
        logger.log(Level.INFO,"用户信息: {0},{1}",new Object[]{name,age});
    }
    //日志级别
    @Test
    public void testLogLever() throws Exception {
        //获取日志记录器
        Logger logger = Logger.getLogger("com.dhcc.JULTest.testLogLever");
        //日志记录输出
        logger.severe("serere");
        logger.warning("warning");
        logger.info("info");
        logger.config("config");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");
    }
    //自定义日志级别
    @Test
    public void testLogConfig() throws Exception {
        //获取日志记录器
        Logger logger = Logger.getLogger("com.dhcc.JULTest.testLogLever");
        //关系系统默认配置
        logger.setUseParentHandlers(false);

        //实现自定义日志级别
        //创建简单的格式转换对象
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        //创建ConsolHhandler 控制台的输出
        ConsoleHandler consoleHandler = new ConsoleHandler();
        //进行关联
        consoleHandler.setFormatter(simpleFormatter);
        logger.addHandler(consoleHandler);

        //配置日志的具体级别
        logger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);

        //创建fileHandler  文件的输出
        FileHandler fileHandler = new FileHandler("jul.log");
        fileHandler.setFormatter(simpleFormatter);
        logger.addHandler(fileHandler);
        fileHandler.setLevel(Level.ALL);
        //日志记录输出
        logger.severe("serere");
        logger.warning("warning");
        logger.info("info");
        logger.config("config");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");
    }
    //logger对象的父子关系
    //儿子默认集成父亲的日志级别
    @Test
    public void  testLogParent() throws Exception {
        Logger logger1 = Logger.getLogger("com.dhcc");
        Logger logger2 = Logger.getLogger("com");
        //java.util.logging.LogManager$RootLogger 所有日志记录器的顶级父元素 名称name 就是 空字符串 “”
        System.out.println(logger1.getParent() == logger2);
        System.out.println("Logger2 Parent:" + logger2.getParent()+"name:"+logger2.getParent().getName());
        //设置logger2的日志级别

        //关系系统默认配置
        logger2.setUseParentHandlers(false);

        //实现自定义日志级别
        //创建简单的格式转换对象
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        //创建ConsolHhandler 控制台的输出
        ConsoleHandler consoleHandler = new ConsoleHandler();
        //进行关联
        consoleHandler.setFormatter(simpleFormatter);
        logger2.addHandler(consoleHandler);

        //配置日志的具体级别
        logger2.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
        //日志记录输出
        logger1.severe("serere");
        logger1.warning("warning");
        logger1.info("info");
        logger1.config("config");
        logger1.fine("fine");
        logger1.finer("finer");
        logger1.finest("finest");
    }
    //加载自定义配置文件
    @Test
    public void testLogProperties() throws Exception {
        //读取配置文件，通过类加载器完成
        InputStream inputStream = JULTest.class.getClassLoader().getResourceAsStream("logging.properties");
        //创建一个loggerManager
        LogManager logManager = LogManager.getLogManager();
        //通过loggerManager加载配置文件
        logManager.readConfiguration(inputStream);
        Logger logger1 = Logger.getLogger("com.dhcc.JULTest.testLogProperties");
        logger1.severe("serere");
        logger1.warning("warning");
        logger1.info("info");
        logger1.config("config");
        logger1.fine("fine");
        logger1.finer("finer");
        logger1.finest("finest");


        Logger logger2 = Logger.getLogger("test");
        logger2.severe("serere");
        logger2.warning("warning");
        logger2.info("info");
        logger2.config("config");
        logger2.fine("fine");
        logger2.finer("finer");
        logger2.finest("finest");

    }
}
