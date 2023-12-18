package com.kasperovich.laelectronics.api.mapping.converters.order;

import com.kasperovich.laelectronics.api.dto.order.OrderGetDto;
import com.kasperovich.laelectronics.api.dto.product.ProductGetDto;
import com.kasperovich.laelectronics.api.mapping.mappers.PaymentMapper;
import com.kasperovich.laelectronics.api.mapping.mappers.ProductListMapper;
import com.kasperovich.laelectronics.models.Category;
import com.kasperovich.laelectronics.models.Order;
import com.kasperovich.laelectronics.models.Product;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderGetConverter implements Converter<Order, OrderGetDto> {

    PaymentMapper paymentMapper;

    ProductListMapper productListMapper;


    @Override
    public OrderGetDto convert(Order order) {
        OrderGetDto orderGetDto = new OrderGetDto();
        orderGetDto.setId(order.getId());
        orderGetDto.setUserId(order.getUser().getId());
        orderGetDto.setPayment(paymentMapper.toDto(order.getPayment()));
        orderGetDto.setTotal(order.getTotal());
        orderGetDto.setStatus(order.getOrderStatus());
        List<Product>products=order.getProducts().stream().toList();
        List<ProductGetDto> productDtoList=productListMapper.toDto(products);
        productDtoList.forEach(x->{
            Optional<Category> category=Optional.ofNullable(products.get(productDtoList.indexOf(x)).getCategory());
            //TODO remove optionality when all products will be assigned to categories
            category.ifPresent(val->x.getCategory().setName(val.getCategoryName()));
        });
        orderGetDto.setProducts(productDtoList);
        return orderGetDto;
    }
}
