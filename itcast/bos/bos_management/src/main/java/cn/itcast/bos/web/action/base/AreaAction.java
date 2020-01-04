package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/22 21:09
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {

    //注入service
    @Autowired
    private AreaService areaService;
    //接受上传文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    //批量区域数据导入
    @Action(value = "area_batchImport")
    public void batchImport() throws Exception {
        List<Area>areas = new ArrayList<Area>();
        //编写解析代码逻辑
        //加载excel文件对象
        Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
        //通过wookbook获得sheet
        Sheet sheet = workbook.getSheetAt(0);
        //遍历sheet中的每一行
        for (Row row : sheet) {
            //跳过表头
            if(row.getRowNum()==0){
                continue;
            }
            //跳过空行
            if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            //获得表格数据
            Area area = new Area();
            area.setId(row.getCell(0).getStringCellValue());
            area.setProvince(row.getCell(1).getStringCellValue());
            area.setCity(row.getCell(2).getStringCellValue());
            area.setDistrict(row.getCell(3).getStringCellValue());
            area.setPostcode(row.getCell(4).getStringCellValue());
            // 基于pinyin4j生成城市编码和简码
            String province = area.getProvince();
            String city = area.getCity();
            String district = area.getDistrict();
            province = province.substring(0, province.length() - 1);
            city = city.substring(0, city.length() - 1);
            district = district.substring(0, district.length() - 1);
            //简码
            String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
            StringBuffer stringBuffer = new StringBuffer();
            for (String head:headArray){
                stringBuffer.append(head);
            }
            String shortcode = stringBuffer.toString();
            area.setShortcode(shortcode);
            //城市编码
            String citycode = PinYin4jUtils.hanziToPinyin(city, "");
            area.setCitycode(citycode);
            areas.add(area);
        }
        //调用业务层 ·
        areaService.saveBatch(areas);
    }
    //分页查询
    @Action(value = "area_pageQuery" ,results = {@Result(name = "success",location = "/pages/base/area.html")})
    public void pageQuery() throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        Pageable pageable = new PageRequest(page - 1, rows);
        //构造条件查询对象
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //创建保存条件集合
                List<Predicate> list = new ArrayList<Predicate>();
                //添加条件
                if(StringUtils.isNoneBlank(model.getProvince())){
                    //根据省份条件查询
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    list.add(p1);
                }
                if (StringUtils.isNoneBlank(model.getCity())){
                    //根据城市查询
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNoneBlank(model.getDistrict())){
                    //根据区域查询
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        //调用业务层  完成查询
        Page<Area>pageData = areaService.findPageData(specification,pageable);
        //将结果转化为json
        PagetoMapToJson(pageData);
    }
}