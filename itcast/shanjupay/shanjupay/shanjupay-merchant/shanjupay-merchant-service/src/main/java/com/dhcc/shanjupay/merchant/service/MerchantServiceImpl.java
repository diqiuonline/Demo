package com.dhcc.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhcc.shanjupay.common.util.StringUtil;
import com.dhcc.shanjupay.merchant.api.dto.StaffDTO;
import com.dhcc.shanjupay.merchant.convert.MerchantConvert;
import com.dhcc.shanjupay.merchant.convert.StaffConvert;
import com.dhcc.shanjupay.merchant.convert.StoreConvert;
import com.dhcc.shanjupay.merchant.entity.Merchant;
import com.dhcc.shanjupay.merchant.entity.Staff;
import com.dhcc.shanjupay.merchant.entity.Store;
import com.dhcc.shanjupay.merchant.entity.StoreStaff;
import com.dhcc.shanjupay.merchant.mapper.MerchantMapper;
import com.dhcc.shanjupay.merchant.mapper.StaffMapper;
import com.dhcc.shanjupay.merchant.mapper.StoreMapper;
import com.dhcc.shanjupay.merchant.api.MerchantService;
import com.dhcc.shanjupay.merchant.api.dto.MerchantDTO;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import com.dhcc.shanjupay.common.util.PhoneUtil;
import com.dhcc.shanjupay.merchant.api.dto.StoreDTO;
import com.dhcc.shanjupay.merchant.mapper.StoreStaffMapper;
import com.dhcc.shanjupay.user.api.TenantService;
import com.dhcc.shanjupay.user.api.dto.tenant.CreateTenantRequestDTO;
import com.dhcc.shanjupay.user.api.dto.tenant.TenantDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:09
 */
@Service
@Transactional
public class MerchantServiceImpl implements MerchantService  {
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private StoreStaffMapper storeStaffMapper;

    @Reference
    private TenantService tenantService;


    @Override
    public MerchantDTO queryMerchantById(Long id) {
        Merchant merchant = merchantMapper.selectById(id);

        MerchantDTO merchantDTO = MerchantConvert.INSTANCE.entityToDto(merchant);

        return merchantDTO;
    }

    /**
     * 插入商户
     * 调用saas接口新增租户，用户，绑定租户和用户，初始化权限
     * @param merchantDTO
     * @return
     */
    @Override
    //@Transactional
    public MerchantDTO createMerchant(MerchantDTO merchantDTO) throws BusinessException{
        //校验参数的合法性
        if (StringUtils.isEmpty(merchantDTO)) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (StringUtils.isEmpty(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (StringUtils.isEmpty(merchantDTO.getPassword())) {
            throw new BusinessException(CommonErrorCode.E_100111);
        }
        if (!PhoneUtil.isMatches(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        //校验手机号的唯一 根据手机号查询商户表 如果存在记录则说明手机号已存在
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", merchantDTO.getMobile());
        Integer count = merchantMapper.selectCount(queryWrapper);
        /*throw new BusinessException(CommonErrorCode.E_100109);*/
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        //调用saas接口  构造调用参数
        CreateTenantRequestDTO createTenantRequestDTO = new CreateTenantRequestDTO();
        createTenantRequestDTO.setMobile(merchantDTO.getMobile());
        createTenantRequestDTO.setUsername(merchantDTO.getUsername());
        createTenantRequestDTO.setPassword(merchantDTO.getPassword());
        createTenantRequestDTO.setTenantTypeCode("shanju-merchant"); //租户类型
        createTenantRequestDTO.setBundleCode("shanju-merchant"); //默认套餐 权限
        createTenantRequestDTO.setName(merchantDTO.getUsername());
        //如果租户在saas已经存在 saas直接返回此租户的信息 否则 进行添加
        TenantDTO tenantDTO = tenantService.createTenantAndAccount(createTenantRequestDTO);
        //获取租户id
        if (StringUtils.isEmpty(tenantDTO) || StringUtils.isEmpty(tenantDTO.getId())) {
            throw new BusinessException(CommonErrorCode.E_200012);
        }
        //根据租户id从商户表中查询 如果存在记录 则不允许添加  租户id在商户表唯一
        Integer integer = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>().eq(Merchant::getTenantId, tenantDTO.getId()));
        if (integer > 0) {
            throw new BusinessException(CommonErrorCode.E_200017);
        }
        //调用mapper想数据库写入记录
        Merchant merchant = MerchantConvert.INSTANCE.dtoToEntity(merchantDTO);
        merchant.setAuditStatus("0");
        //设置商户对应的租户的id
        merchant.setTenantId(tenantDTO.getId());
        merchantMapper.insert(merchant);
        //新增门店
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreName("根门店 ");
        storeDTO.setMerchantId(merchant.getId());
        storeDTO.setStoreStatus(true);
        StoreDTO store = createStore(storeDTO);
        //新增员工
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setMobile(merchantDTO.getMobile()); //手机号
        staffDTO.setUsername(merchantDTO.getUsername()); //账号
        staffDTO.setStoreId(store.getId()); //所属门店id
        staffDTO.setMerchantId(merchant.getId()); //所属商户id
        staffDTO.setStaffStatus(true);
        StaffDTO staff = createStaff(staffDTO);

        //为门店设置管理员
        bindStaffToStore(store.getId(),staff.getId());

        return MerchantConvert.INSTANCE.entityToDto(merchant);
    }

    /**
     * 资质申请
     *
     * @param merchantId  商户id
     * @param merchantDTO 资质信息
     * @throws BusinessException
     */
    @Override
    public void applyMerchant(Long merchantId, MerchantDTO merchantDTO) throws BusinessException {
        if (StringUtils.isEmpty(merchantId) || StringUtils.isEmpty(merchantDTO)) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        //校验商户id的合法性
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (StringUtils.isEmpty(merchant)) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        Merchant merchantEnitiy = MerchantConvert.INSTANCE.dtoToEntity(merchantDTO);
        merchantEnitiy.setId(merchantId);
        merchantEnitiy.setMobile(merchant.getMobile());  //资质申请的时候 手机号不用填
        merchantEnitiy.setAuditStatus("1");  //状态待审核
        merchantEnitiy.setTenantId(merchant.getTenantId());
        merchantMapper.updateById(merchantEnitiy);
    }

    @Override
    public StoreDTO createStore(StoreDTO storeDTO) throws BusinessException {
        if (StringUtils.isEmpty(storeDTO) || StringUtils.isEmpty(storeDTO.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        Store store = StoreConvert.INSTANCE.dto2entity(storeDTO);
        storeMapper.insert(store);
        return StoreConvert.INSTANCE.entity2dto(store);
    }

    /**
     * 新增员工
     *
     * @param staffDTO
     * @return
     * @throws BusinessException
     */
    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) throws BusinessException {
        //参数合法性校验
        if (StringUtils.isEmpty(staffDTO) || StringUtils.isEmpty(staffDTO.getMobile()) || StringUtils.isEmpty(staffDTO.getUsername())|| StringUtils.isEmpty(staffDTO.getStoreId())) {
              throw new BusinessException(CommonErrorCode.E_300009);
        }
        //用一个商户下 员工的账号和手机号唯一
        boolean exisStaffByMobile = isExisStaffByMobile(staffDTO.getMobile(), staffDTO.getMerchantId());
        if (exisStaffByMobile) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        boolean exisStaffByUserName = isExisStaffByUserName(staffDTO.getUsername(), staffDTO.getMerchantId());
        if (exisStaffByUserName) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }
        Staff staff = StaffConvert.INSTANCE.dto2entity(staffDTO);

        staffMapper.insert(staff);
        return StaffConvert.INSTANCE.entity2dto(staff);
    }

    @Override
    public void bindStaffToStore(Long storeId, Long staffID) throws BusinessException {
        StoreStaff storeStaff = new StoreStaff();
        storeStaff.setStaffId(staffID);
        storeStaff.setStoreId(storeId);
        storeStaffMapper.insert(storeStaff);
    }

    @Override
    public MerchantDTO queryMerchantByTenantId(Long tenantId) {
        Merchant merchant = merchantMapper.selectOne(new LambdaQueryWrapper<Merchant>().eq(Merchant::getTenantId, tenantId));
        return MerchantConvert.INSTANCE.entityToDto(merchant);
    }

    //校验手机号是否存在
    private boolean isExisStaffByMobile(String mobile, Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (StringUtils.isEmpty(merchant) ) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
        staffQueryWrapper.eq("MERCHANT_ID", merchantId);
        staffQueryWrapper.eq("MOBILE", mobile);
        Integer integer = staffMapper.selectCount(staffQueryWrapper);
        return integer > 0;
    }

    //校验账号是否存在
    private boolean isExisStaffByUserName(String username, Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (StringUtils.isEmpty(merchant) ) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
        staffQueryWrapper.eq("MERCHANT_ID", merchantId);
        staffQueryWrapper.eq("USERNAME", username);
        Integer integer = staffMapper.selectCount(staffQueryWrapper);
        return integer > 0;
    }

}
