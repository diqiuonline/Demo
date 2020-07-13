package com.dhcc.shanjupay.user.convert;

import com.dhcc.shanjupay.user.api.dto.tenant.CreateTenantRequestDTO;
import com.dhcc.shanjupay.user.entity.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TenantRequestConvert {

    TenantRequestConvert INSTANCE = Mappers.getMapper(TenantRequestConvert.class);

    CreateTenantRequestDTO entity2dto(Tenant entity);

    Tenant dto2entity(CreateTenantRequestDTO dto);
}
