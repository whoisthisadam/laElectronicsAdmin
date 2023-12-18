package com.kasperovich.laelectronics.api.dto.users;

import com.kasperovich.laelectronics.api.dto.discount.DiscountGetDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGetDto {

    Long id;

    String firstName;

    String lastName;

    String mobilePhone;

    String email;

    String login;

    String roleName;

    DiscountGetDto discount;

}
