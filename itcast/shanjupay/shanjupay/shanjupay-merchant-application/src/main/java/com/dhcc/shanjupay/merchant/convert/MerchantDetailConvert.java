package com.dhcc.shanjupay.merchant.convert;

import com.dhcc.shanjupay.merchant.vo.MerchantDetailVO;
import com.dhcc.shanjupay.merchant.vo.MerchantRegisterVO;
import com.dhcc.shanjupay.merchat.api.dto.MerchantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/15 1:40
 */
@Mapper //对象属性映射
public interface MerchantDetailConvert {
    //Logger log = LoggerFactory.getLogger(MerchantConvert.class);
    //创建一个转换实例
    MerchantDetailConvert INSTANCE = Mappers.getMapper(MerchantDetailConvert.class);

    //把dto转换为vo
    MerchantDetailVO dtoToVO(MerchantDTO merchantDTO);

    //把vo转为位dto
    MerchantDTO voToDto(MerchantDetailVO merchantDetailVO);




}
