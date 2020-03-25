package com.dhcc.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/2/14 22:06
 *
 * common-log   如果项目有log4j  使用log4j  如果没有log4j  使用java默认jul
 * 通过循环遍历数组load到的  第一个log4f  第二个jdk14.。也就是jul
 */
public class TestJcl {
    static Log log = LogFactory.getLog("jcl");
    public static void main(String[] args) {
        log.debug("jcl");
    }
}
