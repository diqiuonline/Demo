package cn.itcast.bos.service.take_delivery.impl;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User: 李锦卓
 * Time: 2018/8/30 19:43
 */
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    //输入dao
    @Autowired
    private PromotionRepository promotionRepository;
    //保存数据
    @Override
    public void save(Promotion model) {
        promotionRepository.save(model);

    }
    //查询数据
    @Override
    public Page<Promotion> findQuery(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }
    //根据page和rows返回分页数据
    @Override
    public PageBean<Promotion> findPageData(Integer page, Integer rows) {
        Pageable pageable = new PageRequest(page-1,rows);
        Page<Promotion> pageData = promotionRepository.findAll(pageable);
        //封装到page对象
        PageBean<Promotion>pageBean = new PageBean<Promotion>();
        pageBean.setPageData(pageData.getContent());  //当前页数据
        pageBean.setTotalCount(pageData.getTotalElements()); //总页数
        return pageBean;
    }
    //根据id查询promotion对昂
    @Override
    public Promotion findByI(Integer id) {
        return promotionRepository.findOne(id);
    }
    //每分钟执行一次当前时间大于promotion 中的endDate 活动已经过期 设置status = 2
    @Override
    public void updateStatus(Date date) {
        promotionRepository.updateStatus(date);
    }
}