package com.kasperovich.laelectronics.api.mapping.converters.discount;

import com.kasperovich.laelectronics.api.dto.discount.DiscountCreateDto;
import com.kasperovich.laelectronics.models.Discount;
import com.kasperovich.laelectronics.models.Edit;
import com.kasperovich.laelectronics.models.User;
import com.kasperovich.laelectronics.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiscountCreateConverter implements Converter<DiscountCreateDto, Discount> {

    UserRepository userRepository;

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
        return discount;
    }
}
