package com.kasperovich.laelectronics.api.mapping.converters.user;

import com.kasperovich.laelectronics.api.dto.users.UserCreateDto;
import com.kasperovich.laelectronics.api.mapping.mappers.AddressMapper;
import com.kasperovich.laelectronics.exception.BadPasswordException;
import com.kasperovich.laelectronics.models.Address;
import com.kasperovich.laelectronics.models.Credentials;
import com.kasperovich.laelectronics.models.User;
import com.kasperovich.laelectronics.repository.UserRepository;
import com.kasperovich.laelectronics.util.ValidCheck;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserUpdateConverter implements Converter<UserCreateDto, User> {

    private final UserRepository userRepository;

    private final AddressMapper addressMapper;

    private final ValidCheck validCheck;

    PasswordEncoder encoder=new BCryptPasswordEncoder();


    @Override
    public User convert(UserCreateDto userCreateDto) {
        return null;
    }

    public User doConvert(UserCreateDto userCreateDto, Long id) throws EntityNotFoundException, BadPasswordException {
        Optional<Credentials> newCredentials = Optional.ofNullable(userCreateDto.getCredentials());
        if (newCredentials.isPresent()) {
            Optional<String> newPassword = Optional.ofNullable(newCredentials.get().getPassword());
            if (newPassword.isPresent() &&
                    !validCheck.isPasswordValid(newPassword.get())) {
                throw new BadPasswordException("Password must include at least one capital, or number, or symbol");
            }
        }
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with this ID does not exist!"));
        if (newCredentials.isPresent()) {
            newCredentials.get().setLogin(Optional.ofNullable(newCredentials.get().getLogin()).orElse(user.getCredentials().getLogin()));
            Optional<String>newPassword = Optional.ofNullable(newCredentials.get().getPassword());
            if(newPassword.isPresent())newCredentials.get().setPassword(encoder.encode(newPassword.get()));
            else newCredentials.get().setPassword(user.getCredentials().getPassword());
            user.setCredentials(newCredentials.get());
        }
        user.setFirstName(
                Optional.ofNullable(userCreateDto.getFirstName()).orElse(user.getFirstName())
        );
        user.setLastName(
                Optional.ofNullable(userCreateDto.getLastName()).orElse(user.getLastName())
        );
        user.setEmail(
                Optional.ofNullable(userCreateDto.getEmail()).orElse(user.getEmail())
        );
        user.setMobilePhone(
                Optional.ofNullable(userCreateDto.getMobilePhone()).orElse(user.getMobilePhone())
        );
        Address address=user.getAddress();
        if(Optional.ofNullable(userCreateDto.getAddress()).isPresent()){
            address=addressMapper.toEntity(userCreateDto.getAddress());
            address.setPostcode(userCreateDto.getAddress().getPostCode());
        }
        user.setAddress(
                address
        );
        return user;
    }
}
