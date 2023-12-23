package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.enums.OrderStatus;
import com.kasperovich.laelectronics.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
