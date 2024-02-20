package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.product.DeleteProductDto;
import com.kasperovich.laelectronics.api.dto.product.SubscriptionCreateDto;
import com.kasperovich.laelectronics.api.dto.product.SubscriptionGetDto;
import com.kasperovich.laelectronics.api.mapping.converters.product.SubscriptionUpdateConverter;
import com.kasperovich.laelectronics.api.mapping.mappers.SubscriptionListMapper;
import com.kasperovich.laelectronics.api.mapping.mappers.SubscriptionMapper;
import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.models.Organization;
import com.kasperovich.laelectronics.models.Subscription;
import com.kasperovich.laelectronics.repository.OrganizationRepository;
import com.kasperovich.laelectronics.repository.SubscriptionsRepository;
import com.kasperovich.laelectronics.service.subscription.SubscriptionService;
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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@Slf4j
@RequestMapping("/data/products")
@RequiredArgsConstructor
@Tag(name = "Subscriptions")
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionsController {

    SubscriptionService subscriptionService;

    SubscriptionMapper subscriptionMapper;

    SubscriptionListMapper subscriptionListMapper;

    SubscriptionUpdateConverter subscriptionUpdateConverter;

    SubscriptionsRepository subscriptionsRepository;

    OrganizationRepository organizationRepository;

    @Operation(
            summary = "Gets all products",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found products",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SubscriptionGetDto.class))))
            })
    @GetMapping
    public ResponseEntity<List<SubscriptionGetDto>>findAll(){
        List<Subscription>entites= subscriptionService.findAll();
        List<SubscriptionGetDto>dtos=new ArrayList<>(subscriptionListMapper.toDto(entites));
        dtos.forEach(dto->{
            dto.setManufacturerName(entites.get(dtos.indexOf(dto)).getOrganization().getName());
        });
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Create product(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = SubscriptionGetDto.class)))
                            })
            }
            ,parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    @PostMapping
    public ResponseEntity<Map<String, SubscriptionGetDto> >createProduct(@RequestBody SubscriptionCreateDto subscriptionCreateDto){
        Subscription product= subscriptionMapper.toEntity(subscriptionCreateDto);
        product.setOrganization(organizationRepository.findByName(subscriptionCreateDto.getManufacturerName()).orElseGet(()->{
            Organization manufacturer=Organization.builder().name(subscriptionCreateDto.getManufacturerName()).build();
            organizationRepository.save(manufacturer);
            return manufacturer;
        }));
        Subscription createdProduct= subscriptionService.createProduct(product);
        SubscriptionGetDto subscriptionGetDto = subscriptionMapper.toDto(createdProduct);
        return new ResponseEntity<>(
                Collections.singletonMap("New product:", subscriptionGetDto),
                HttpStatus.CREATED
        );
    }


    @Operation(
            summary = "Update product(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SubscriptionGetDto.class))))
            },parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    @PatchMapping("/update")
    public ResponseEntity<Map<String, SubscriptionGetDto>>updateProduct(@RequestParam String id, @RequestBody SubscriptionCreateDto subscriptionCreateDto){
        Long iD=Long.parseLong(id);
        Subscription product= subscriptionUpdateConverter.doConvert(subscriptionCreateDto, iD);
        Subscription updatedProduct= subscriptionService.updateProduct(product);
        return ResponseEntity.ok(Collections.singletonMap("Updated product:", subscriptionMapper.toDto(updatedProduct)));
    }

    @Operation(
            summary = "Find all products by date(Admin&Moderator only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = SubscriptionGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/dated")
    public ResponseEntity<List<SubscriptionGetDto>> findAllDated(@RequestParam Timestamp from, @RequestParam Timestamp to) {
        List<Subscription>entites= subscriptionService
                .findAll()
                .stream()
                .filter(product -> product.getEditData().getCreationDate().after(from)&&product.getEditData().getCreationDate().before(to))
                .toList();
        List<SubscriptionGetDto>dtos=new ArrayList<>(subscriptionListMapper.toDto(entites));
        dtos.forEach(x->{
            x.setManufacturerName(entites.get(dtos.indexOf(x)).getOrganization().getName());
        });
        return ResponseEntity.ok(dtos);
    }


    @Operation(
            summary = "Delete product(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product deleted",
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
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/delete")
    public ResponseEntity<DeleteProductDto>deleteProduct(@RequestParam(value = "ID") String idStr) throws NotDeletableStatusException {
        Long id=Long.parseLong(idStr);
        subscriptionService.deleteProduct(id);
        return ResponseEntity.ok(new DeleteProductDto(DeleteProductDto.DeletedStatus.DELETED, id));
    }

    @Operation(
            summary = "Find product by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product returned",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SubscriptionGetDto.class))))
            })
    @GetMapping("/product")
    public ResponseEntity<SubscriptionGetDto>findProductById(@RequestParam(value = "ID") String id) {
        Subscription product= subscriptionsRepository.findProductByIdAndIsDeleted(Long.parseLong(id), false)
                .orElseThrow(()->new EntityNotFoundException("Product with this ID does not exist"));
        SubscriptionGetDto subscriptionGetDto =new SubscriptionGetDto(
                product.getId(),
                product.getName(),
                product.getOrganization().getName(),
                product.getPrice()
        );
        return ResponseEntity.ok(subscriptionGetDto);
    }

}
