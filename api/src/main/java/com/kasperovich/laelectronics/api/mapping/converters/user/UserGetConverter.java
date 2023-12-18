package com.kasperovich.laelectronics.api.mapping.converters.user;

import com.kasperovich.laelectronics.api.dto.users.UserGetDto;
import com.kasperovich.laelectronics.models.User;
import org.springframework.core.convert.converter.Converter;

public class UserGetConverter implements Converter<User, UserGetDto> {


    @Override
    public UserGetDto convert(User source) {
        return UserGetDto
                .builder()
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .mobilePhone(source.getMobilePhone())
                .login(source.getCredentials().getLogin())
                .email(source.getEmail())
                .build();
    }
}
