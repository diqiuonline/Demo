package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

/**
 * User: 李锦卓
 * Time: 2018/8/19 21:40
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	//注入dao
	@Autowired
	private CourierRepository courierRepository;
	//添加快递员
	@Override
	public void save(Courier courier) {
		courierRepository.save(courier);
	}
	//查询快递员
	@Override
    @RequiresPermissions("region:list")
	public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
		return courierRepository.findAll(specification,pageable);
	}
	//作废快递员
	@Override
	public void delBeath(String[] idArrer) {
		//调用dao实现update操作 将deltag修改为1
		for(String idstr:idArrer){
		    Integer id = Integer.parseInt(idstr);
		    courierRepository.delBeath(id);
        }
	}
    //恢复快递员
    @Override
    public void redBeath(String[] idArrer) {
        for(String idstr:idArrer){
            Integer id = Integer.parseInt(idstr);
            courierRepository.redBeath(id);
        }
    }
    //查询所有未关联的快递员
    @Override
    public List<Courier> findNoAssociation() {
	    Specification<Courier>specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //查询条件 查询列表size为空
                Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
                return p;
            }
        };
        List<Courier> list = courierRepository.findAll(specification);
        return list;
    }

}