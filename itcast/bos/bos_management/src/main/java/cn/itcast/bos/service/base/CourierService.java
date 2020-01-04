package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.crm.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CourierService {
	//添加快递员
	void save(Courier courier);
	//查询快递员
	Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);
	//作废快递员
	void delBeath(String[] idArrer);
	//回复快递员
	void redBeath(String[] idArrer);
	//查询所有未关联定区的快递员
	List<Courier> findNoAssociation();

}
