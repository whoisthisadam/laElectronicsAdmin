package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.users.DeleteUserDto;
import com.kasperovich.laelectronics.api.dto.users.UserCreateDto;
import com.kasperovich.laelectronics.api.dto.users.UserGetDto;
import com.kasperovich.laelectronics.api.mapping.converters.discount.DiscountGetConverter;
import com.kasperovich.laelectronics.api.mapping.converters.user.UserUpdateConverter;
import com.kasperovich.laelectronics.api.mapping.mappers.UserMapper;
import com.kasperovich.laelectronics.exception.BadPasswordException;
import com.kasperovich.laelectronics.models.User;
import com.kasperovich.laelectronics.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@Slf4j
@RequestMapping("/data/users")
@RequiredArgsConstructor
@Tag(name = "Users")
@CacheConfig(cacheNames = "users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserUpdateConverter userUpdateConverter;

    private final DiscountGetConverter discountGetConverter;

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
    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserGetDto>>findAll(){
        List<User>users=userService.findAll();
        List<UserGetDto>userGetDtos=users
                .stream()
                .map(
                        x->{
                            UserGetDto userGetDto=userMapper.toDto(x);
                            userGetDto.setLogin(x.getCredentials().getLogin());
                            userGetDto.setRoleName(String.valueOf(x.getRole().getName()));
                            if(x.getUserDiscount()!=null){
                                userGetDto.setDiscount(discountGetConverter.convert(x.getUserDiscount()));
                            }
                            return userGetDto;
                        }
                ).toList();
        return ResponseEntity.ok(userGetDtos);
    }

    @Operation(
            summary = "Update user(Admin&Own Data only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated",
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
    @PreAuthorize(value = "hasRole('ADMIN') or authentication.principal.username.equals(#userCreateDto.email)" )
    @Transactional
    @CachePut
    @PatchMapping("/update")
    public ResponseEntity<Map<String, UserGetDto>>updateUser(@RequestParam String id, @RequestBody UserCreateDto userCreateDto) throws BadPasswordException {
        User user= userUpdateConverter.doConvert(userCreateDto, Long.parseLong(id));
        UserGetDto result=userMapper.toDto(userService.updateUser(user));
        result.setRoleName(String.valueOf(user.getRole().getName()));
        result.setLogin(user.getCredentials().getLogin());
        return new ResponseEntity<>(
                Collections.singletonMap(
                        "Updated user:", result
        ),HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user(Admin&Own Data only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation =String.class))))
            } ,parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize(value = "hasRole('ADMIN') or @userSecurity.hasUserId(authentication, #id)")
    @CacheEvict
    @PatchMapping("/delete")
    public ResponseEntity<DeleteUserDto>deleteUser(@RequestParam String id){
        Long iD=Long.parseLong(id);
        userService.deleteUser(iD);
        return ResponseEntity.ok(new DeleteUserDto(DeleteUserDto.DeletedStatus.DELETED, iD));
    }

    @Operation(
            summary = "Get user's profile(Admin&Own Data only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile returned",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation =String.class))))
            } ,parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize(value = "hasRole('ADMIN') or authentication.principal.username.equals(#email)")
    @GetMapping("/user")
    public ResponseEntity<UserGetDto>getProfile(@RequestParam String email){
        User user=userService.findUserByEmail(email);
        UserGetDto result=userMapper.toDto(user);
        result.setRoleName(String.valueOf(user.getRole().getName()));
        result.setLogin(user.getCredentials().getLogin());
        return ResponseEntity.ok(result);
    }
}
