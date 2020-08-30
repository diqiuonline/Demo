package com.dhcc.shanjupay.merchant.api;

import com.dhcc.shanjupay.common.domain.PageVO;
import com.dhcc.shanjupay.merchant.api.dto.MerchantDTO;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.merchant.api.dto.StaffDTO;
import com.dhcc.shanjupay.merchant.api.dto.StoreDTO;
import com.dhcc.shanjupay.merchant.api.dto.StoreStaffDTO;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:01
 */
public interface MerchantService {
    //根据id查询商户
    MerchantDTO queryMerchantById(Long id);
    //注册商户 接受账号密码  使用MerchantDTO接受数据
    MerchantDTO createMerchant(MerchantDTO merchantDTO) throws BusinessException;

    //资质申请
    void applyMerchant(Long merchantId, MerchantDTO merchantDTO) throws BusinessException;

    //新增门店

    StoreDTO createStore(StoreDTO storeDTO) throws BusinessException;

    //新增员工
    StaffDTO createStaff(StaffDTO staffDTO) throws BusinessException;

    //将员工设置位门店管理员
    void bindStaffToStore(Long storeId,Long StaffID) throws BusinessException;
    //根据租户id查询商户信息
    MerchantDTO queryMerchantByTenantId(Long tenantId);
    /**
     * 门店列表的查询
     * @param storeDTO 查询条件，必要参数：商户id
     * @param pageNo  页码
     * @param pageSize 分页记录数
     * @return
     */
    PageVO<StoreDTO> queryStoreByPage(StoreDTO storeDTO, Integer pageNo, Integer pageSize);

    /**
     * 校验门店是否属于商户
     * @param StoreId
     * @param merchantId
     * @return
     */
    Boolean queryStoreInMerchant(Long StoreId,Long merchantId);
}
