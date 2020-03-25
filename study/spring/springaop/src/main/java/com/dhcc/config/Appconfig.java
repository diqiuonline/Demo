package com.dhcc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/2/14 23:46
 */
@Configuration
@ComponentScan("com.dhcc")
@EnableAspectJAutoProxy
public class Appconfig {

}
    //实现了bean的嵌套注入

