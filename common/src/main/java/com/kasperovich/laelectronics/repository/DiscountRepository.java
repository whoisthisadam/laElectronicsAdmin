package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.enums.Discounts;
import com.kasperovich.laelectronics.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Discount findDiscountByNameAndIsDeletedIsFalse(Discounts name);

}
