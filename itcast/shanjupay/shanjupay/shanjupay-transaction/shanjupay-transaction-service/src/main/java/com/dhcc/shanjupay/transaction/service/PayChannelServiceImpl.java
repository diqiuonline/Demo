package com.dhcc.shanjupay.transaction.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhcc.shanjupay.common.cache.Cache;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import com.dhcc.shanjupay.common.domain.ErrorCode;
import com.dhcc.shanjupay.common.util.RedisUtil;
import com.dhcc.shanjupay.merchat.api.AppService;
import com.dhcc.shanjupay.merchat.api.dto.AppDTO;
import com.dhcc.shanjupay.transaction.api.PayChannelService;
import com.dhcc.shanjupay.transaction.api.dto.PayChannelDTO;
import com.dhcc.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.dhcc.shanjupay.transaction.api.dto.PlatformChannelDTO;
import com.dhcc.shanjupay.transaction.common.util.RedisCache;
import com.dhcc.shanjupay.transaction.convert.PayChannelParamConvert;
import com.dhcc.shanjupay.transaction.convert.PlatformChannelConvert;
import com.dhcc.shanjupay.transaction.entity.AppPlatformChannel;
import com.dhcc.shanjupay.transaction.entity.PayChannelParam;
import com.dhcc.shanjupay.transaction.entity.PlatformChannel;
import com.dhcc.shanjupay.transaction.mapper.AppPlatformChannelMapper;
import com.dhcc.shanjupay.transaction.mapper.PayChannelParamMapper;
import com.dhcc.shanjupay.transaction.mapper.PlatformChannelMapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.security.KeyFactory;
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
    @Autowired
    private PayChannelParamMapper payChannelParamMapper;

    @Reference
    private AppService appService;
    @Autowired
    private Cache cache;



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

    @Override
    public void savePayChannelParam(PayChannelParamDTO payChannelParamDTO) throws BusinessException {
        //根据应用、服务类型 支付渠道 查询一条记录
        //1 根据应用 服务类型 查询绑定id 在根据绑定id和支付渠道查询paychannelparam

        Long appPlatformChannelId = selectIdByAppPlatformChannel(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());
        QueryWrapper<PayChannelParam> payChannelParamWrapper = new QueryWrapper<>();
        payChannelParamWrapper.eq("PAY_CHANNEL", payChannelParamDTO.getPayChannel());
        payChannelParamWrapper.eq("APP_PLATFORM_CHANNEL_ID", appPlatformChannelId);
        PayChannelParam payChannelParam = payChannelParamMapper.selectOne(payChannelParamWrapper);
        //2 判断非空
        if (!StringUtils.isEmpty(payChannelParam)) {
            payChannelParam.setChannelName(payChannelParamDTO.getChannelName());
            payChannelParam.setParam(payChannelParamDTO.getParam());
            payChannelParamMapper.updateById(payChannelParam);
        } else {
            PayChannelParam entity = PayChannelParamConvert.INSTANCE.dto2entity(payChannelParamDTO);
            entity.setId(null);
            entity.setAppPlatformChannelId(appPlatformChannelId);  //应用与服务关系绑定id
            payChannelParamMapper.insert(entity);

        }
        //保存到redis
        updatreCache(payChannelParamDTO.getAppId(),payChannelParamDTO.getPlatformChannelCode());

    }

    @Override
    public List<PayChannelParamDTO> queryPayChannelParamByAppAndPlatform(String appId, String platformChannel) {
        //先从redis中查询 如果有 在返回
        String redisKey = RedisUtil.keyBuilder(appId, platformChannel);
        Boolean exists = cache.exists(redisKey);
        if (exists) {
            return JSON.parseArray(cache.get(redisKey), PayChannelParamDTO.class);
        }
        Long appPlatformChannelId = selectIdByAppPlatformChannel(appId, platformChannel);
        QueryWrapper<PayChannelParam> payChannelParamQueryWrapper = new QueryWrapper<>();
        payChannelParamQueryWrapper.eq("APP_PLATFORM_CHANNEL_ID", appPlatformChannelId);
        List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(payChannelParamQueryWrapper);
        if (StringUtils.isEmpty(payChannelParams)) {
            return null;
        }

        List<PayChannelParamDTO> payChannelParamDTOS = PayChannelParamConvert.INSTANCE.listentity2listdto(payChannelParams);
        cache.set(redisKey, JSON.toJSON(payChannelParamDTOS).toString());
        return payChannelParamDTOS;

    }

    @Override
    public PayChannelParamDTO queryParamByAppPlatformAndPayChannel(String appId, String platfromChannel, String payChannel) throws BusinessException {
        List<PayChannelParamDTO> payChannelParamDTOS = queryPayChannelParamByAppAndPlatform(appId, platfromChannel);
        for (PayChannelParamDTO payChannelParamDTO : payChannelParamDTOS) {
            if (payChannel.equals(payChannelParamDTO.getPayChannel())) {
                return payChannelParamDTO;
            }
        }
        return null;
    }

    /**
     * 根据应用和服务类型将查询到的支付渠道参数存入redis
     * @param appId 应用id
     * @param platformChannelCode 支付渠道参数
     */
    private void updatreCache(String appId, String platformChannelCode) {
        //根据应用id和服务类型code 查询支付渠道参数列表 将数据写入redis
        List<PayChannelParamDTO> payChannelParamDTOS = queryPayChannelParamByAppAndPlatform(appId, platformChannelCode);
        //得到redis中的key 支付渠道参数列表的key 格式 支付渠道参数：应用id：服务类型code
        String redisKey = RedisUtil.keyBuilder(appId, platformChannelCode);
        //根据key 查询redis
        Boolean exists = cache.exists(redisKey);
        if (exists) {
            cache.del(redisKey);
        }
        if (StringUtils.isEmpty(payChannelParamDTOS)) {
             cache.set(redisKey, JSON.toJSON(payChannelParamDTOS).toString());
        }


    }
    /**
     * 根据应用、服务类型查询应用与服务类型的绑定id
     * @param appId
     * @param platformChannelCode
     * @return
     */
    private Long selectIdByAppPlatformChannel(String appId,String platformChannelCode){
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>().eq(AppPlatformChannel::getAppId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannelCode));
        if(appPlatformChannel!=null){
            return appPlatformChannel.getId();//应用与服务类型的绑定id
        }
        throw new BusinessException(CommonErrorCode.E_300010);
    }
}
