package com.dhcc.shanjupay.transaction.convert;

import com.dhcc.shanjupay.transaction.api.dto.PayOrderDTO;
import com.dhcc.shanjupay.transaction.entity.PayOrder;
import com.dhcc.shanjupay.transaction.vo.OrderConfirmVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayOrderConvert {

    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrderDTO entity2dto(PayOrder entity);

    PayOrder dto2entity(PayOrderDTO dto);

    //忽略totalAmount，不做映射
    @Mapping(target = "totalAmount", ignore = true)
    PayOrderDTO vo2dto(OrderConfirmVO vo);

}
