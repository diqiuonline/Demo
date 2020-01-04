package cn.itcast.bos.service.take_delivery;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.ws.rs.*;
import java.util.Date;

public interface PromotionService {
    //保存数据
    void save(Promotion model);
    //查询
    Page<Promotion> findQuery(Pageable pageable);
    //根据page和rows返回分页数据
    @Path("/pageQuery")
    @GET
    @Produces({"application/xml", "application/json"})
    PageBean<Promotion>findPageData(
            @QueryParam("page") Integer page,
            @QueryParam("rows") Integer rows
    );
    //根据id查询
    @Path("/promotion/{id}")
    @GET
    @Produces({"application/xml", "application/json"})
    Promotion findByI(@PathParam("id") Integer id);

    //每分钟执行一次当前时间大于promotion 中的endDate 活动已经过期 设置status = 2
    void updateStatus(Date date);

}
