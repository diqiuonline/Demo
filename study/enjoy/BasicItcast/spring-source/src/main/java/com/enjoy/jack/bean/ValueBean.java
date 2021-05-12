package com.enjoy.jack.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
@Data
public class ValueBean  implements EnvironmentAware{

    @Value("${enjoy.name}")
    private String name;


    @Override
    public void setEnvironment(Environment environment) {
        System.out.println(name);
        System.out.println(environment.getProperty("enjoy.name"));
    }
}
