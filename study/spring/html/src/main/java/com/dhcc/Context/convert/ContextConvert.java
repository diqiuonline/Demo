package com.dhcc.Context.convert;

import org.mapstruct.factory.Mappers;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/7/15 0:19
 */
public interface ContextConvert {
    ContextConvert INSTANCE = Mappers.getMapper(ContextConvert.class);

}
