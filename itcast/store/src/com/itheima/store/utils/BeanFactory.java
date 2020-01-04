package com.itheima.store.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.applet.Main;

/**
 * User: 李锦卓
 * Time: 2018/7/11 10:18
 */
public class BeanFactory {
    public  static Object getBean(String id){
        SAXReader reader = new SAXReader();
        try {
            //解析xml
            Document document = reader.read(BeanFactory.class.getClassLoader().getResourceAsStream("applicationContext.xml"));
            //获得class内容
            Element beanElement = (Element)document.selectSingleNode("//bean[@id='"+id+"']");
            String value = beanElement.attributeValue("class");
            //反射生成实例
            Class clazz = Class.forName(value);
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
