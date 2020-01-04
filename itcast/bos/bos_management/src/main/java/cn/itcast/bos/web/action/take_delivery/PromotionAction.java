package cn.itcast.bos.web.action.take_delivery;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * User: 李锦卓
 * Time: 2018/8/30 19:36
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
    //属性驱动
    private File titleImgFile;
    private String titleImgFileFileName;
    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }
    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }
    //注入
    @Autowired
    private PromotionService promotionService;
    @Action(value = "promotion_save", results = {@Result(name = "success",type = "redirect", location = "/pages/take_delivery/promotion.html")})
    public String save() throws IOException {
        //宣传图上传 在数据表中保存宣传图的路径
        String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
        String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
        //生成随机图片名
        UUID uuid = UUID.randomUUID();
        String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
        String randomName = uuid + ext;
        //保存图片 绝对路径
        File destFile = new File(savePath + "/" + randomName);
        System.out.println(destFile.getAbsolutePath());
        FileUtils.copyFile(titleImgFile, destFile);
        //将保存路劲 相对路径web工程项目 保存到 model中
        model.setTitleImg(saveUrl + randomName);
        //调用业务层 完成数据保存
        promotionService.save(model);
        return SUCCESS;
    }
    //属性驱动
    private Integer rows;
    private Integer page;

    @Override
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Action(value = "promotion_pageQuery")
    public void PageQuery() throws IOException {
        //构造分页查询参数
        Pageable pageable = new PageRequest(page-1,rows);
        //调用业务层完成查询
        Page<Promotion>promotions = promotionService.findQuery(pageable);
        PagetoMapToJson(promotions);

    }

}