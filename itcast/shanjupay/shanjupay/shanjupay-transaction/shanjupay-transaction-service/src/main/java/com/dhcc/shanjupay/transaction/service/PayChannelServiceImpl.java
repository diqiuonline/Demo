package com.dhcc.shanjupay.transaction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import com.dhcc.shanjupay.merchat.api.AppService;
import com.dhcc.shanjupay.merchat.api.dto.AppDTO;
import com.dhcc.shanjupay.transaction.api.PayChannelService;
import com.dhcc.shanjupay.transaction.api.dto.PayChannelDTO;
import com.dhcc.shanjupay.transaction.api.dto.PlatformChannelDTO;
import com.dhcc.shanjupay.transaction.convert.PlatformChannelConvert;
import com.dhcc.shanjupay.transaction.entity.AppPlatformChannel;
import com.dhcc.shanjupay.transaction.entity.PlatformChannel;
import com.dhcc.shanjupay.transaction.mapper.AppPlatformChannelMapper;
import com.dhcc.shanjupay.transaction.mapper.PlatformChannelMapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/7/8 19:30
 */
@Service
public class PayChannelServiceImpl implements PayChannelService {
    @Autowired
    private PlatformChannelMapper platformChannelMapper;
    @Autowired
    private AppPlatformChannelMapper appPlatformChannelMapper;

    @Reference
    private AppService appService;

    @Autowired

    //查询所有的服务平台服务
    @Override
    public List<PlatformChannelDTO> queryPlatformChannerl() throws BusinessException {
        List<PlatformChannel> platformChannels = platformChannelMapper.selectList(null);
        return PlatformChannelConvert.INSTANCE.listentity2listdto(platformChannels);
    }

    @Override
    public void bindPlatformChannelForApp(String appId, String platformChannelCodes) throws BusinessException {

        AppDTO appById = appService.getAppById(appId);
        if (StringUtils.isEmpty(appById)) {
            throw new BusinessException(CommonErrorCode.E_120101);
        }
        QueryWrapper<PlatformChannel> wrapperPlatformChanner = new QueryWrapper<>();
        wrapperPlatformChanner.eq("CHANNEL_CODE", platformChannelCodes);
        PlatformChannel platformChannel = platformChannelMapper.selectOne(wrapperPlatformChanner);
        if (StringUtils.isEmpty(platformChannel)) {
            throw new BusinessException(CommonErrorCode.E_120102);
        }
        //根据应用id和服务code查询 如果找不到 插入
        QueryWrapper<AppPlatformChannel> wrapper = new QueryWrapper<>();
        wrapper.eq("APP_ID", appId);
        wrapper.eq("PLATFORM_CHANNEL", platformChannelCodes);
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(appPlatformChannel)) {
            appPlatformChannelMapper.insert(new AppPlatformChannel(null, appId, platformChannelCodes));
        } else {
            throw new BusinessException(CommonErrorCode.E_300008);
        }
    }

    @Override
    public int queryAppBindPlatformChannel(String appId, String platformChannelCodes) throws BusinessException {
        AppDTO appById = appService.getAppById(appId);
        if (StringUtils.isEmpty(appById)) {
            throw new BusinessException(CommonErrorCode.E_120101);
        }
        QueryWrapper<PlatformChannel> wrapperPlatformChanner = new QueryWrapper<>();
        wrapperPlatformChanner.eq("CHANNEL_CODE", platformChannelCodes);
        PlatformChannel platformChannel = platformChannelMapper.selectOne(wrapperPlatformChanner);
        if (StringUtils.isEmpty(platformChannel)) {
            throw new BusinessException(CommonErrorCode.E_120102);
        }

        QueryWrapper<AppPlatformChannel> wrapper = new QueryWrapper<>();
        wrapper.eq("APP_ID", appId);
        wrapper.eq("PLATFORM_CHANNEL", platformChannelCodes);
        Integer integer = appPlatformChannelMapper.selectCount(wrapper);
        if (integer > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode) throws BusinessException {
        //调用mapper
        return platformChannelMapper.selectPayChannelByPlatformChannel(platformChannelCode);
    }
}
