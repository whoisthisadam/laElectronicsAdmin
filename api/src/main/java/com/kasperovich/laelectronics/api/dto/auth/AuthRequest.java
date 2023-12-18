package com.kasperovich.laelectronics.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AuthRequest {

    @Schema(defaultValue = "arinakasperrr", type = "string", description = "User Login OR Email")
    @NotBlank
    private String emailOrLogin;

    @Schema(defaultValue = "arinak2006", type = "string", description = "User password")
    @NotBlank
    private String password;

}
