package com.dhcc.shanjupay.merchant.convert;

import com.dhcc.shanjupay.merchant.entity.App;
import com.dhcc.shanjupay.merchat.api.dto.AppDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/7/5 22:06
 */
@Mapper
public interface AppCovert {
    //创建一个转换实例
    AppCovert INSTANCE = Mappers.getMapper(AppCovert.class);

    //将dto转换为entity
    App dtoToEntity(AppDTO appDTO);

    //entity 转换为 app
    AppDTO entityToDto(App app);

    List<AppDTO> listentityToDto (List<App> app);

}
