package com.dhcc.shanjupay.merchant.convert;

import com.dhcc.shanjupay.merchant.entity.Merchant;
import com.dhcc.shanjupay.merchant.api.dto.MerchantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/15 1:40
 */
@Mapper //对象属性映射
public interface MerchantConvert {
    //Logger log = LoggerFactory.getLogger(MerchantConvert.class);
    //创建一个转换实例
    MerchantConvert INSTANCE = Mappers.getMapper(MerchantConvert.class);

    //把dto转换为entity
    Merchant dtoToEntity(MerchantDTO merchantDTO);

    //把entity转为位dto
    MerchantDTO entityToDto(Merchant merchant);


}
