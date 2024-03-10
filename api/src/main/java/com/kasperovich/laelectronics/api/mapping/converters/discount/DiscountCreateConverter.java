package com.kasperovich.laelectronics.api.mapping.converters.discount;

import com.kasperovich.laelectronics.api.dto.discount.DiscountCreateDto;
import com.kasperovich.laelectronics.models.Discount;
import com.kasperovich.laelectronics.models.Edit;
import com.kasperovich.laelectronics.models.Subscription;
import com.kasperovich.laelectronics.models.User;
import com.kasperovich.laelectronics.repository.SubscriptionsRepository;
import com.kasperovich.laelectronics.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiscountCreateConverter implements Converter<DiscountCreateDto, Discount> {

    UserRepository userRepository;

    SubscriptionsRepository subscriptionsRepository;

    @Override
    public Discount convert(DiscountCreateDto discountCreateDto) {
        Discount discount = new Discount();
        discount.setName(discountCreateDto.getName());
        discount.setDiscountPercent(discountCreateDto.getDiscountPercent());
        discount.setEditData(new Edit(new Timestamp(new Date().getTime()), null));
        if (discountCreateDto.getUserIds() != null) {
            Set<User> users = new HashSet<>(userRepository.findAllById(discountCreateDto.getUserIds()));
            discount.setUsers(users);
            users.forEach(x -> x.setUserDiscount(discount));
        }
        if (discountCreateDto.getSubId()!=null){
            Subscription subscription=subscriptionsRepository.findProductByIdAndIsDeleted(discountCreateDto.getSubId(), false)
                    .orElseThrow(()->new EntityNotFoundException("Subscription not found"));

            if (discount.getSubscriptions()!=null) {
                discount.getSubscriptions().add(subscription);
            }
            else{
                discount.setSubscriptions(Set.of(subscription));
            }
            subscription.setSubDiscount(discount);
            subscriptionsRepository.save(subscription);
        }
        return discount;
    }
}
