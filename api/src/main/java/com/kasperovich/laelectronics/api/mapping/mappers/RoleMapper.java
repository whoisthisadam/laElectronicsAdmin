package com.kasperovich.laelectronics.api.mapping.mappers;

import com.kasperovich.laelectronics.api.dto.roles.RoleCreateDto;
import com.kasperovich.laelectronics.api.dto.roles.RoleGetDto;
import com.kasperovich.laelectronics.models.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleGetDto toResponse(Role role);

    Role toEntity(RoleCreateDto roleRequest);

}
