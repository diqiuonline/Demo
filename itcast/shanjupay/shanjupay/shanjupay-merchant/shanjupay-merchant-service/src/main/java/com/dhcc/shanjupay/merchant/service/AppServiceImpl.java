package com.dhcc.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhcc.shanjupay.merchant.convert.AppCovert;
import com.dhcc.shanjupay.merchant.entity.App;
import com.dhcc.shanjupay.merchant.entity.Merchant;
import com.dhcc.shanjupay.merchant.mapper.AppMapper;
import com.dhcc.shanjupay.merchant.mapper.MerchantMapper;
import com.dhcc.shanjupay.merchant.api.AppService;
import com.dhcc.shanjupay.merchant.api.dto.AppDTO;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/29 22:13
 */
@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public AppDTO createApp(Long merchantId, AppDTO appDTO) throws BusinessException {
        //调用appmapper 向应用表插入信息
        /**
         * 1 校验商户是否通过资质申请 如果资质申请没有通过不允许创建应用
         * 2 生成应用id  使用uuid的方式
         * 3 保存商户的引用信息 应用名称需要校验一致性
         */
        if (StringUtils.isEmpty(merchantId) || StringUtils.isEmpty(appDTO)|| StringUtils.isEmpty(appDTO.getAppName())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (StringUtils.isEmpty(merchant)) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        //取出商户资质申请状态
        String auditStatus = merchant.getAuditStatus();
        if (!"2".equals(auditStatus)) {
            throw new BusinessException(CommonErrorCode.E_200003);
        }
        //生成uud
        String uuid = String.valueOf(UUID.randomUUID());
        //校验商户名称唯一
        boolean existAppName = isExistAppName(appDTO.getAppName());
        if (existAppName) {
            throw new BusinessException(CommonErrorCode.E_200004);
        }
        //插入
        App app = AppCovert.INSTANCE.dtoToEntity(appDTO);
        app.setAppId(uuid);
        app.setMerchantId(merchantId);

        appMapper.insert(app);
        return AppCovert.INSTANCE.entityToDto(app);
    }

    @Override
    public List<AppDTO> queryAppByMerchant(Long merchantId) throws BusinessException {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.eq("MERCHANT_ID", merchantId);
        List<App> apps = appMapper.selectList(wrapper);
        List<AppDTO> appDTOS = AppCovert.INSTANCE.listentityToDto(apps);
        return appDTOS;
    }

    @Override
    public AppDTO getAppById(String appId) throws BusinessException {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.eq("APP_ID", appId);
        App app = appMapper.selectOne(wrapper);
        return AppCovert.INSTANCE.entityToDto(app);
    }

    private boolean isExistAppName(String appName) {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.eq("APP_NAME", appName);
        Integer integer = appMapper.selectCount(wrapper);
        return integer > 0;
    }
}
