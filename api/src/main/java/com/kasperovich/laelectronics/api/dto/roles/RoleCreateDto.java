package com.kasperovich.laelectronics.api.dto.roles;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateDto {

    String name;

}
