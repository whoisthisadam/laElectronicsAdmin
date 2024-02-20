package com.kasperovich.laelectronics.api.mapping.mappers;

import com.kasperovich.laelectronics.api.dto.product.SubscriptionCreateDto;
import com.kasperovich.laelectronics.api.dto.product.SubscriptionGetDto;
import com.kasperovich.laelectronics.models.Subscription;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = SubscriptionMapper.class)
public interface SubscriptionListMapper {

    List<Subscription> toEntityList(List<SubscriptionCreateDto> dtos);

    List<SubscriptionGetDto> toDto(List<Subscription> products);

}
