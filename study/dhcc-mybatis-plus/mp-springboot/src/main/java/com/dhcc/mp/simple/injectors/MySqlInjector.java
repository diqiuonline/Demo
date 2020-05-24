package com.dhcc.mp.simple.injectors;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/24 20:32
 */

public class MySqlInjector extends DefaultSqlInjector  {

    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> list = new ArrayList<>();
        //获取父类中的集合
        List<AbstractMethod> methodList = super.getMethodList();
        list.addAll(methodList);
        //扩充自定义实现
        list.add(new FindAll());
        return list;
    }
}
