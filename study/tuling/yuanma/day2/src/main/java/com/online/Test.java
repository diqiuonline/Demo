package com.online;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author 李锦卓
 * @Description TODO
 * @Date 2022/8/10 11:09
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
   /*     OnlineApplicationContext applicationContext = new OnlineApplicationContext(AppConfig.class);
        UserInterface userService = (UserInterface) applicationContext.getBean("userService");
        //UserService userService1 = (UserService) applicationContext.getBean("userService");
        OrderService orderService = (OrderService) applicationContext.getBean("orderService");
        OrderService orderService1 = (OrderService) applicationContext.getBean("orderService");*/
        //userService.test();
        System.out.println(new BCryptPasswordEncoder().encode("nacos"));

        //System.out.println(userService);
        /*System.out.println(userService1);
        System.out.println(orderService1);
        System.out.println(orderService);*/

    }
}
