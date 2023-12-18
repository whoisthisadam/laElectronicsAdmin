package com.kasperovich.laelectronics.api.mapping.converters.discount;

import com.kasperovich.laelectronics.api.dto.discount.DiscountGetDto;
import com.kasperovich.laelectronics.models.Discount;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiscountGetConverter implements Converter<Discount, DiscountGetDto> {

    @Override
    public DiscountGetDto convert(Discount discount) {
        DiscountGetDto discountGetDto=new DiscountGetDto();
        discountGetDto.setId(discount.getId());
        discountGetDto.setName(discount.getName());
        discountGetDto.setPercent(discount.getDiscountPercent());
        return discountGetDto;
    }


}
