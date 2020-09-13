package com.dhcc.copywork.convert;

import com.dhcc.copywork.entity.PlayCate;
import com.dhcc.copywork.entity.PlayCateTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayCateConvert {
    PlayCateConvert INSTANCE = Mappers.getMapper(PlayCateConvert.class);
    
    PlayCate templateToEntiey(PlayCateTemplate playCateTemplate);
}
