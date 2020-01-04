package cn.itcast.bos.web.action;

import cn.itcast.bos.domain.page.PageBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/8/22 23:40
 */
public abstract class BaseAction<T>  extends ActionSupport implements ModelDriven<T> {
    //模型驱动
    public T model;
    @Override
    public T getModel() {
        return model;
    }
    //构造器 完成model实例化
    public BaseAction(){
        //构造子类action对象 获取继承夫类型的泛型
        //AreaAction extends BaseAction《Area》
        // BaseAction《Area》
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        //获取类型第一个泛型参数
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Class<T>ModelClass = (Class<T>)parameterizedType.getActualTypeArguments()[0];
        try {
            model = ModelClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //接受分页查询参数
    public Integer rows;
    public Integer page;

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    // 将分页查询结果数据，转换Map在转json的方法
    public void PagetoMapToJson(Page<T> pageData) throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        String string = JSONObject.toJSONString(result);
        ServletActionContext.getResponse().getWriter().println(string);
    }
    //将list集合转换为json
    public void ListToJson(List<T>list) throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        String string = JSONObject.toJSONString(list);
        ServletActionContext.getResponse().getWriter().println(string);
    }
    //将map集合转化为json
    public void MaptoJson(Map<String,Object>map) throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        String string = JSONObject.toJSONString(map);
        ServletActionContext.getResponse().getWriter().println(string);
    }
    //将page转换json
    public void PageToJson(Page<T> pageData) throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        String string = JSONObject.toJSONString(pageData);
        ServletActionContext.getResponse().getWriter().println(string);
    }
    //将自定义pageBean转换json
    public void pageBeanToJson(PageBean<T> pageBean) throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        String string = JSON.toJSONStringWithDateFormat(pageBean,"yyyy-MM-dd");
        ServletActionContext.getResponse().getWriter().println(string);
    }
}