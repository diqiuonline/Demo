package com.dhcc.mvc;


/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/16 20:21
 */
/*public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //初始化spring环境
        System.out.println("124");
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(AppConfig.class);
        ac.refresh();

        DispatcherServlet ds = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registrationion = servletContext.addServlet("app", ds);
        registrationion.setLoadOnStartup(1);
        registrationion.addMapping("/");
    }
}*/
