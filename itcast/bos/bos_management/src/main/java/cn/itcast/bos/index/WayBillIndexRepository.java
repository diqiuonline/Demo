package cn.itcast.bos.index;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * User: 李锦卓
 * Time: 2018/9/8 16:40
 */
public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill,Integer> {
}