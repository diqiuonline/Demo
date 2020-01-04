package cn.itcast.bos.service.take_delivery.impl;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/9/4 23:07
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    @Autowired
    private WayBillRepository wayBillRepository;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    //保存工单
    @Override
    public void save(WayBill model) {
        //判断运单号是否存在
        WayBill persistWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
        if (persistWayBill == null || persistWayBill.getId() == null) {
            //运单不存在
            wayBillRepository.save(model);
            //保存索引
            wayBillIndexRepository.save(model);
        } else {
            //运单存在
            try {
                if (persistWayBill.getSignStatus() == 1) {
                    Integer id = persistWayBill.getId();
                    BeanUtils.copyProperties(persistWayBill, model);
                    persistWayBill.setId(id);
                    persistWayBill.setSignStatus(1);//代发货
                    //保存索引
                    wayBillIndexRepository.save(model);
                } else {
                    throw new RuntimeException("运单已发出 不能修改");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    //根据运单号查询运单
    @Override
    public WayBill findOne(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }

    //条件查询
    @Override
    public Page<WayBill> findPage(WayBill wayBill, Pageable pageable) {
        //判断waybull中条件是否存在
            if (StringUtils.isBlank(wayBill.getWayBillNum())
                    && StringUtils.isBlank(wayBill.getSendAddress())
                    && StringUtils.isBlank(wayBill.getRecAddress())
                    && StringUtils.isBlank(wayBill.getSendProNum())
                    && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
                //无条件查询
                return wayBillIndexRepository.findAll(pageable);
            } else {
                //查询条件
                //must条件必须成立and
                //must no 条件必须不成立 not
                //should 条件可以成立 or
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();//布尔查询 多条件查询 组合查询
                if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
                    //运单号查询 等值查询
                    QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                    boolQueryBuilder.must(queryBuilder);
                }
                if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
                    //发货地查询 模糊查询
                    //情况一 输入北  是查询词条一部分 使用模糊匹配词条查询
                    QueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");
                    //情况二 输入北京市海定区 十多个词条组合 进行分此后 每个词条匹配查询
                    QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress())
                            .field("sendAddress").defaultOperator(Operator.AND);
                    BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                    boolQueryBuilder1.should(wildcardQueryBuilder);
                    boolQueryBuilder1.should(queryStringQueryBuilder);
                    boolQueryBuilder.must(boolQueryBuilder1);
                }
                if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                    //收货地查询 模糊查询
                    //情况一 输入北  是查询词条一部分 使用模糊匹配词条查询
                    QueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");
                    //情况二 输入北京市海定区 十多个词条组合 进行分此后 每个词条匹配查询
                    QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress())
                            .field("recAddress").defaultOperator(Operator.AND);
                    BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                    boolQueryBuilder1.should(wildcardQueryBuilder);
                    boolQueryBuilder1.should(queryStringQueryBuilder);
                    boolQueryBuilder.must(boolQueryBuilder1);
                }
                if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                    //类型查询 等值查询
                    QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                    boolQueryBuilder.must(queryBuilder);
                }
                if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                    //牵手状态查询 等值查询
                    QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                    boolQueryBuilder.must(queryBuilder);
                }
            SearchQuery searchQuery = new NativeSearchQuery(boolQueryBuilder);
            searchQuery.setPageable(pageable);//分页效果
            Page<WayBill> search = wayBillIndexRepository.search(searchQuery);
            return search;
        }
    }

    @Override
    public WayBill findByOrderId(Integer id) {
        return wayBillRepository.findByOrderId(id);
    }
    //导出xls表格
    @Override
    public List<WayBill> findWayBills(WayBill wayBill) {
        //判断waybull中条件是否存在
        if (StringUtils.isBlank(wayBill.getWayBillNum())
                && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress())
                && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            //无条件查询
            return wayBillRepository.findAll();
        } else {
            //查询条件
            //must条件必须成立and
            //must no 条件必须不成立 not
            //should 条件可以成立 or
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();//布尔查询 多条件查询 组合查询
            if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
                //运单号查询 等值查询
                QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                boolQueryBuilder.must(queryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
                //发货地查询 模糊查询
                //情况一 输入北  是查询词条一部分 使用模糊匹配词条查询
                QueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");
                //情况二 输入北京市海定区 十多个词条组合 进行分此后 每个词条匹配查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress())
                        .field("sendAddress").defaultOperator(Operator.AND);
                BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                boolQueryBuilder1.should(wildcardQueryBuilder);
                boolQueryBuilder1.should(queryStringQueryBuilder);
                boolQueryBuilder.must(boolQueryBuilder1);
            }
            if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                //收货地查询 模糊查询
                //情况一 输入北  是查询词条一部分 使用模糊匹配词条查询
                QueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");
                //情况二 输入北京市海定区 十多个词条组合 进行分此后 每个词条匹配查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress())
                        .field("recAddress").defaultOperator(Operator.AND);
                BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                boolQueryBuilder1.should(wildcardQueryBuilder);
                boolQueryBuilder1.should(queryStringQueryBuilder);
                boolQueryBuilder.must(boolQueryBuilder1);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                //类型查询 等值查询
                QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                boolQueryBuilder.must(queryBuilder);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                //牵手状态查询 等值查询
                QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                boolQueryBuilder.must(queryBuilder);
            }
            SearchQuery searchQuery = new NativeSearchQuery(boolQueryBuilder);
            // ElasticSearch 允许搜索分页查询，最大数据条数10000
            Pageable pageable = new PageRequest(0, 10000);
            searchQuery.setPageable(pageable); // 分页效果

            // 有条件查询 、查询索引库
            return wayBillIndexRepository.search(searchQuery).getContent();
        }

    }
}