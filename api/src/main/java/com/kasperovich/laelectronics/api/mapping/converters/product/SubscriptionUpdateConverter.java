package com.kasperovich.laelectronics.api.mapping.converters.product;

import com.kasperovich.laelectronics.api.dto.product.SubscriptionCreateDto;
import com.kasperovich.laelectronics.models.Organization;
import com.kasperovich.laelectronics.models.Subscription;
import com.kasperovich.laelectronics.repository.OrganizationRepository;
import com.kasperovich.laelectronics.repository.SubscriptionsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionUpdateConverter implements Converter<SubscriptionCreateDto, Subscription> {

    SubscriptionsRepository subscriptionsRepository;

    OrganizationRepository organizationRepository;


    @Override
    public Subscription convert(SubscriptionCreateDto subscriptionCreateDto) {
        return null;
    }

    public Subscription doConvert(SubscriptionCreateDto subscriptionCreateDto, Long id) throws EntityNotFoundException {
        Subscription product = subscriptionsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product with this id does not exist"));
        product.setId(id);

        product.setName(
                Optional.ofNullable(subscriptionCreateDto.getName()).orElse(product.getName())
        );
        Optional.ofNullable(subscriptionCreateDto.getManufacturerName()).ifPresentOrElse(name->
            product.setOrganization(organizationRepository.findByName(name).orElseGet(()->{
                Organization manufacturer = Organization.builder().name(name).build();
                organizationRepository.save(manufacturer);
                return manufacturer;
            })),
                ()-> product.setOrganization(product.getOrganization())
        );
        product.setPrice(
                Optional.ofNullable(subscriptionCreateDto.getPrice()).orElse(product.getPrice())
        );
        product.setIsAvailable(
                Optional.ofNullable(
                                subscriptionCreateDto.getIsAvailable())
                        .orElse(product.getIsAvailable())
        );
        return product;
    }
}
