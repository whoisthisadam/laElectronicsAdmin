package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.discount.DiscountCreateDto;
import com.kasperovich.laelectronics.api.dto.discount.DiscountGetDto;
import com.kasperovich.laelectronics.api.dto.order.OrderGetDto;
import com.kasperovich.laelectronics.api.mapping.converters.discount.DiscountCreateConverter;
import com.kasperovich.laelectronics.api.mapping.converters.discount.DiscountGetConverter;
import com.kasperovich.laelectronics.models.Discount;
import com.kasperovich.laelectronics.service.discount.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Validated
@RequestMapping("/data/discounts")
@RequiredArgsConstructor
@Tag(name = "discounts")
public class DiscountController {

    private final DiscountService discountService;

    private final DiscountCreateConverter discountCreateConverter;

    private final DiscountGetConverter discountGetConverter;

    @Operation(
            summary = "Create discount(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    @PostMapping
    public ResponseEntity<Map<String, DiscountGetDto>> createDiscount(@RequestBody DiscountCreateDto discountCreateDto) {
        Discount discount = discountCreateConverter.convert(discountCreateDto);
        discountService.createDiscount(discount);
        assert discount != null;
        return new ResponseEntity<>(Collections.singletonMap("Created discount:", discountGetConverter.convert(discount)), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Find all discounts(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<DiscountGetDto>> findAllDiscount() {
        List<DiscountGetDto> list = discountService.findAll()
                .stream().map(discountGetConverter::convert).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Delete discount(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<String> deleteDiscount(@RequestParam String id) {
        discountService.deleteDiscount(Long.parseLong(id));
        return ResponseEntity.ok("Discount with id " + id + " deleted");
    }


}
