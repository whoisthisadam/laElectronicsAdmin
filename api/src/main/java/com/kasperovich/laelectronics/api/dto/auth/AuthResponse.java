package com.kasperovich.laelectronics.api.dto.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {

    String userNameOrEmail;

    String token;

    Long userId;

    String role;

}
