package com.enjoy.jack.bean.scanBean;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@ComponentScan("com.enjoy.jack")
//<context:property-placeholder location="classpath:application.properties"/>
@PropertySource(name = "jack",value = "classpath:application.properties")
public class ScanBean {
}
