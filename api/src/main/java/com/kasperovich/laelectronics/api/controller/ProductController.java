package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.category.CategoryDto;
import com.kasperovich.laelectronics.api.dto.product.DeleteProductDto;
import com.kasperovich.laelectronics.api.dto.product.ProductCreateDto;
import com.kasperovich.laelectronics.api.dto.product.ProductGetDto;
import com.kasperovich.laelectronics.api.mapping.converters.product.ProductUpdateConverter;
import com.kasperovich.laelectronics.api.mapping.mappers.ProductListMapper;
import com.kasperovich.laelectronics.api.mapping.mappers.ProductMapper;
import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.models.Category;
import com.kasperovich.laelectronics.models.Product;
import com.kasperovich.laelectronics.repository.CategoryRepository;
import com.kasperovich.laelectronics.repository.ProductRepository;
import com.kasperovich.laelectronics.service.product.ProductService;
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

import java.util.*;

@RestController
@Validated
@Slf4j
@RequestMapping("/data/products")
@RequiredArgsConstructor
@Tag(name = "Products")
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    ProductMapper productMapper;

    ProductListMapper productListMapper;

    ProductUpdateConverter productUpdateConverter;

    CategoryRepository categoryRepository;

    ProductRepository productRepository;

    @Operation(
            summary = "Gets all products",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found products",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProductGetDto.class))))
            })
    @GetMapping
    public ResponseEntity<List<ProductGetDto>>findAll(){
        List<Product>entites=productService.findAll();
        List<ProductGetDto>dtos=new ArrayList<>(productListMapper.toDto(entites));
        dtos.forEach(x->{
            Optional<Category> category=Optional.ofNullable(entites.get(dtos.indexOf(x)).getCategory());
            //TODO remove optionality when all products will be assigned to categories
            category.ifPresent(val->x.getCategory().setName(val.getCategoryName()));
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
                                            array = @ArraySchema(schema = @Schema(implementation = ProductGetDto.class)))
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
    public ResponseEntity<Map<String, ProductGetDto> >createProduct(@RequestBody ProductCreateDto productCreateDto){
        Product product=productMapper.toEntity(productCreateDto);
        String catName=productCreateDto.getCategory().getName();
        product.setCategory(
                categoryRepository.findCategoryByCategoryName(catName).orElseThrow(()->new EntityNotFoundException("Category "+catName+" not found"))
        );
        Product createdProduct=productService.createProduct(product);
        ProductGetDto productGetDto=productMapper.toDto(createdProduct);
        productGetDto.getCategory().setName(createdProduct.getCategory().getCategoryName());
        return new ResponseEntity<>(
                Collections.singletonMap("New product:", productGetDto),
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
                                    array = @ArraySchema(schema = @Schema(implementation = ProductGetDto.class))))
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
    public ResponseEntity<Map<String, ProductGetDto>>updateProduct(@RequestParam String id, @RequestBody ProductCreateDto productCreateDto){
        Long iD=Long.parseLong(id);
        Product product=productUpdateConverter.doConvert(productCreateDto, iD);
        Product updatedProduct=productService.updateProduct(product);
        ProductGetDto productGetDto=productMapper.toDto(updatedProduct);
        productGetDto.getCategory().setName(updatedProduct.getCategory().getCategoryName());
        return ResponseEntity.ok(Collections.singletonMap("Updated product:",productMapper.toDto(product)));
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
        productService.deleteProduct(id);
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
                                    array = @ArraySchema(schema = @Schema(implementation =ProductGetDto.class))))
            })
    @GetMapping("/product")
    public ResponseEntity<ProductGetDto>findProductById(@RequestParam(value = "ID") String id) {
        Product product=productRepository.findProductByIdAndIsDeleted(Long.parseLong(id), false)
                .orElseThrow(()->new EntityNotFoundException("Product with this ID does not exist"));
        ProductGetDto productGetDto=new ProductGetDto(
                new CategoryDto(product.getCategory().getCategoryName()),
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getPrice()
        );
        return ResponseEntity.ok(productGetDto);
    }

}
