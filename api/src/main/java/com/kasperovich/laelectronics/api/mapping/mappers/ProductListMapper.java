package com.kasperovich.laelectronics.api.mapping.mappers;

import com.kasperovich.laelectronics.api.dto.product.ProductCreateDto;
import com.kasperovich.laelectronics.api.dto.product.ProductGetDto;
import com.kasperovich.laelectronics.models.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface ProductListMapper {

    List<Product> toEntityList(List<ProductCreateDto> dtos);

    List<ProductGetDto> toDto(List<Product> products);

}
