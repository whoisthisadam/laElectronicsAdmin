package com.kasperovich.laelectronics.api.dto.roles;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoleGetDto {

    Long id;

    String name;

}
