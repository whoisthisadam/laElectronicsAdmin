package com.kasperovich.laelectronics.api.mapping.mappers;


import com.kasperovich.laelectronics.api.dto.users.UserCreateDto;
import com.kasperovich.laelectronics.api.dto.users.UserGetDto;
import com.kasperovich.laelectronics.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {

    List<UserGetDto> toDtoList(List<User>userList);

    List<User>toEntityList(List<UserCreateDto>dtoList);

}
