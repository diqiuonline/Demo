package com.xuecheng.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * User: 李锦卓
 * Time: 2019/6/21 15:25
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class encoder {
    @Test
    public void demo(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePWD =encoder.encode("XcWebApp"); //加密完之后赋值给encodePWD
        System.out.println(encodePWD);
        boolean xcWebApp = encoder.matches("111111","$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe");
        System.out.println(xcWebApp);

    }
}