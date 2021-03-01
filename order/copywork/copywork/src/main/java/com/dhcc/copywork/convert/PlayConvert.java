package com.dhcc.copywork.convert;

import com.dhcc.copywork.entity.Play;
import com.dhcc.copywork.entity.PlayTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayConvert {
    PlayConvert INSTANCE = Mappers.getMapper(PlayConvert.class);

    Play templateToEntiey(PlayTemplate playTemplate);
}
