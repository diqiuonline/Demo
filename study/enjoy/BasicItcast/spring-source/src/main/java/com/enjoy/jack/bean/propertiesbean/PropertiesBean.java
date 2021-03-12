package com.enjoy.jack.bean.propertiesbean;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PropertiesBean {

    private String name;

    private String password;
}
