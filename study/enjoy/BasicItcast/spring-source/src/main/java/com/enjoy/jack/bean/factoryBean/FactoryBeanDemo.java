package com.enjoy.jack.bean.factoryBean;

import com.enjoy.jack.bean.Student;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**

 * 灵活定义需要我们自己创建的实例的时候，我们可以实现factoryBean接口
 * 在getObject方法里面定义实例化过程
 *
 */
@Component
public class FactoryBeanDemo implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return new Student();
    }

    @Override
    public Class<?> getObjectType() {
        return Student.class;
    }
}
