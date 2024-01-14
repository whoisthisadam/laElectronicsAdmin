package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.manufacturer.ManufacturerGetDto;
import com.kasperovich.laelectronics.models.Manufacturer;
import com.kasperovich.laelectronics.repository.ManufacturerRepository;
import com.kasperovich.laelectronics.repository.OrderRepository;
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
@RequestMapping("/data/manufacturers")
@RequiredArgsConstructor
@Tag(name = "Manufacturers")
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManufacturersController {

    ManufacturerRepository manufacturerRepository;

    OrderRepository orderRepository;

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
    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping
    public ResponseEntity<List<ManufacturerGetDto>> findAll() {
        List<Manufacturer> manufacturersEntities = manufacturerRepository.findAll();

        List<ManufacturerGetDto> manufacturers = manufacturersEntities.stream().map(
                manufacturer ->
                        ManufacturerGetDto.builder().name(manufacturer.getName()).productsNumber((long) manufacturer.getProducts().size()).build()

        ).sorted(Comparator.comparing(ManufacturerGetDto::getProductsNumber).reversed()).collect(Collectors.toList());

        return ResponseEntity.ok(manufacturers);
    }

}
