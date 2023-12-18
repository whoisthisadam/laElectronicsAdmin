package com.kasperovich.laelectronics.api.dto.users;

import com.kasperovich.laelectronics.api.dto.address.AddressCreateDto;
import com.kasperovich.laelectronics.models.Credentials;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;



@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDto {

    Credentials credentials;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    String mobilePhone;

    String email;

    AddressCreateDto address;

}
