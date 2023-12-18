package com.kasperovich.laelectronics.service.order;

import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.models.Order;

import java.util.List;


public interface OrderService {

    List<Order> findAll();

    Order createOrder(Order order);

    Order updateOrder(Order order);

    Order deleteOrder(Long id) throws NotDeletableStatusException;

}
