package com.dhcc.test;

import com.dhcc.dao.AccountDao;
import javafx.application.Application;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 李锦卓
 * 2022/2/19 12:15
 * 1.0
 */
public class AccountTest {


    @Test
    public void account() {
        //得到spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        AccountDao accountDap = applicationContext.getBean(AccountDao.class);
        accountDap.findA();
    }
}
