package com.dhcc.mvc;

import com.dhcc.app.AppConfig;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/16 21:04
 */
public class SpringApplication {
    public static void run() {
        //是操作系统的零时目录
        File file = new File(System.getProperty("java.io.tmpdir"));
        //启动内嵌tomcat
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8889);
        //tomcat.addWebapp("/", file.getAbsolutePath());
        tomcat.addContext("/", file.getAbsolutePath());
        try {
            tomcat.start();
            //让tomcat阻塞 等待连接处理

            System.out.println("124");
            AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
            ac.register(AppConfig.class);
            ac.refresh();

            DispatcherServlet ds = new DispatcherServlet(ac);
            Wrapper wrapper = tomcat.addServlet("/", "app", ds);
            //ServletRegistration.Dynamic registrationion = servletContext.addServlet("app", ds);
            wrapper.setLoadOnStartup(1);
            //registrationion.setLoadOnStartup(1);
            wrapper.addMapping("/");
            // registrationion.addMapping("/");


            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

    }

}
