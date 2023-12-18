package com.kasperovich.laelectronics.api.mapping.mappers;

import com.kasperovich.laelectronics.api.dto.users.UserCreateDto;
import com.kasperovich.laelectronics.api.dto.users.UserGetDto;
import com.kasperovich.laelectronics.models.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {

    UserGetDto toDto(User user);

    User toEntity(UserCreateDto userCreateDto);

}

