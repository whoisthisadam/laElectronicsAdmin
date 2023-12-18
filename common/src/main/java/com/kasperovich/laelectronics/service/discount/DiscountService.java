package com.kasperovich.laelectronics.service.discount;

import com.kasperovich.laelectronics.models.Discount;

import java.util.List;

public interface DiscountService {

    List<Discount> findAll();

    Discount createDiscount(Discount discount);

    Discount deleteDiscount(Long id);


}
