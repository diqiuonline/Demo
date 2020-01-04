package cn.itcast.bos.web.action;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/8/30 22:27
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
    @Action(value = "promotion_pageQuery")
    public void pageQuery() throws IOException {
        //基于webservice获取bos_management的活动列表信息
        PageBean<Promotion>pageBean = WebClient.create(Constants.BOS_MANAGEMENT_URL+"/services/promotionService/pageQuery?page="
                + page + "&rows=" + rows).accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        pageBeanToJson(pageBean);
    }

    @Action(value = "promotion_showDetail")
    public void showDetail() throws Exception {
        //先判断id对应的html是否存在 如果存在直接返回
        String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker/");
        File htmlFile = new File(htmlRealPath+"/"+model.getId()+".html");
        //如果htmlfile不存在 查询数据库 结合freemarker模板生成页面
        if (!htmlFile.exists()) {
            //不存在
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            File freemarker_templates = new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates"));
            configuration.setDirectoryForTemplateLoading(freemarker_templates);
            //获取模板数据
            Template template = configuration.getTemplate("promotion_detail.ftl");
            //动态数据
            Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/promotion/"
                    + model.getId()).accept(MediaType.APPLICATION_JSON).get(Promotion.class);
            Map<String,Object>parameteMap = new HashMap<String, Object>();
            parameteMap.put("promotion",promotion);
            //合并输出
            template.process(parameteMap,new OutputStreamWriter(new FileOutputStream(htmlFile),"utf-8"));
        }
        //存在 直接返回文件
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        FileUtils.copyFile(htmlFile,ServletActionContext.getResponse().getOutputStream());
    }
}