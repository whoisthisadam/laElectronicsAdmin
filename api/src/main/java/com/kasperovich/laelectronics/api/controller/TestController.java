package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.users.UserGetDto;
import com.kasperovich.laelectronics.models.Address;
import com.kasperovich.laelectronics.repository.AddressRepository;
import com.kasperovich.laelectronics.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Validated
@Slf4j
@RequestMapping("/test")
@RequiredArgsConstructor
@Tag(name = "Test")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {

    @Data
    class TokenDto{
        UserDetails userDetails;
    }

    UserService userService;

    AddressRepository addressRepository;

    @Operation(
            summary = "Gets all users(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the users",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserGetDto.class))))
            },parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @GetMapping("/user")
    public ResponseEntity<TokenDto> userPrincipalTest(Authentication authentication){
        TokenDto response=new TokenDto();
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        response.setUserDetails(userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/test-address")
    public ResponseEntity<Address> getAddressByEmail(@RequestParam String email)
    {
        Address address = addressRepository.findAddressByUserEmail(email);
        return ResponseEntity.ok(address);
    }
}
