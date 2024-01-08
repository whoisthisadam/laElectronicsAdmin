package com.kasperovich.laelectronics.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AuthRequest {

    @Schema(defaultValue = "john.doe@example.com", type = "string", description = "User Login OR Email")
    @NotBlank
    private String email;

    @Schema(defaultValue = "P@ssw0rd", type = "string", description = "User password")
    @NotBlank
    private String password;

}
