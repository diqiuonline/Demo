package com.dhcc.copywork.convert;

import com.dhcc.copywork.entity.Game;
import com.dhcc.copywork.entity.GameTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface GameConvert {
    GameConvert INSTANCE = Mappers.getMapper(GameConvert.class);

    Game templateToEntiey(GameTemplate gameTemplate);
}
