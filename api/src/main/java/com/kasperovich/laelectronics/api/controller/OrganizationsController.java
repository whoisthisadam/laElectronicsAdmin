package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.manufacturer.OrganizationGetDto;
import com.kasperovich.laelectronics.models.Organization;
import com.kasperovich.laelectronics.repository.OrganizationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@Slf4j
@RequestMapping("/data/organizations")
@RequiredArgsConstructor
@Tag(name = "Organizations")
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrganizationsController {

    OrganizationRepository organizationRepository;

    @Operation(
            summary = "Find selling manufacturers data",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All selling manufacturers",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class))))
            }, parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrganizationGetDto>> findAll() {
        List<Organization> manufacturersEntities = organizationRepository.findAll();

        List<OrganizationGetDto> manufacturers = manufacturersEntities.stream().map(
                manufacturer ->
                        OrganizationGetDto.builder().name(manufacturer.getName()).subNumber((long) manufacturer.getSubscriptions().size()).build()

        ).sorted(Comparator.comparing(OrganizationGetDto::getSubNumber).reversed()).collect(Collectors.toList());

        return ResponseEntity.ok(manufacturers);
    }

}
