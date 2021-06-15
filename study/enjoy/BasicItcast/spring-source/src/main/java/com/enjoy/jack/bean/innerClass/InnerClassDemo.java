package com.enjoy.jack.bean.innerClass;

import com.enjoy.jack.bean.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class InnerClassDemo {

    @Component
    public class SpringSource {

        /*@Bean
        public Student student() {
            return new Student();
        }*/

    }

    @Component
    public class Mybatis {

    }
}
