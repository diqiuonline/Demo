package com.dhcc.shanjupay.user.convert;

import com.dhcc.shanjupay.user.api.dto.tenant.TenantDTO;
import com.dhcc.shanjupay.user.entity.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TenantConvert {

    TenantConvert INSTANCE = Mappers.getMapper(TenantConvert.class);

    TenantDTO entity2dto(Tenant entity);

    Tenant dto2entity(TenantDTO dto);
}
