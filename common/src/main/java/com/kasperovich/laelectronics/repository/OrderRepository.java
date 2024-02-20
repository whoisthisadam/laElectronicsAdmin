package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
