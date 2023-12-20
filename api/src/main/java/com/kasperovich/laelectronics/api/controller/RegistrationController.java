package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.users.UserCreateDto;
import com.kasperovich.laelectronics.api.dto.users.UserGetDto;
import com.kasperovich.laelectronics.api.mapping.converters.discount.DiscountGetConverter;
import com.kasperovich.laelectronics.api.mapping.mappers.UserMapper;
import com.kasperovich.laelectronics.exception.BadPasswordException;
import com.kasperovich.laelectronics.models.User;
import com.kasperovich.laelectronics.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@Validated
@Slf4j
@RequestMapping("/registration")
@RequiredArgsConstructor
@Tag(name = "Registration")
@CrossOrigin
public class RegistrationController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final DiscountGetConverter discountGetConverter;

    @Operation(
            summary = "Register a new User",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserGetDto.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User not registered, Conflict",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "User not registered, Illegal Arguments",
                            content = @Content)
            })
    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, UserGetDto>> addUser(@Valid @RequestBody UserCreateDto userCreateDto) throws BadPasswordException {
        User user = userMapper.toEntity(userCreateDto);
        user.getAddress().setPostcode(userCreateDto.getAddress().getPostCode());
        User savedUser = userService.createUser(user);
        UserGetDto userGetDto=userMapper.toDto(savedUser);
        userGetDto.setLogin(user.getCredentials().getLogin());
        userGetDto.setRoleName(user.getRole().getName().toString());
        if(user.getUserDiscount()!=null){
            userGetDto.setDiscount(discountGetConverter.convert(user.getUserDiscount()));
        }
        return new ResponseEntity<>(Collections.singletonMap("New user:", userGetDto),
                HttpStatus.CREATED);
    }
}
