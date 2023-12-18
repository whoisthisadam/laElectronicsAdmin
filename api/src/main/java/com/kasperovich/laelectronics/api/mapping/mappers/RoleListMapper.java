package com.kasperovich.laelectronics.api.mapping.mappers;

import com.kasperovich.laelectronics.api.dto.roles.RoleCreateDto;
import com.kasperovich.laelectronics.api.dto.roles.RoleGetDto;
import com.kasperovich.laelectronics.models.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface RoleListMapper {

    List<Role>toEntityList(List<RoleCreateDto> roleRequestList);

    List<RoleGetDto>toResponsesList(List<Role>roleList);

}
