package com.dhcc.mp.simple.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/24 22:21
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    //插入数据时填充
    @Override
    public void insertFill(MetaObject metaObject) {
        //先获取到possword的值 判断 如果为空 就进行填充 如果不为空 不填充
        Object password = getFieldValByName("password", metaObject);
        if (StringUtils.isEmpty(password)) {
            setFieldValByName("password", "88888888", metaObject);
        }
    }
    //更新数据时填充
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
