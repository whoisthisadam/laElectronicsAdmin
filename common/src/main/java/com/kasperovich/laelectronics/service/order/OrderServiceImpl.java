package com.kasperovich.laelectronics.service.order;

import com.kasperovich.laelectronics.enums.OrderStatus;
import com.kasperovich.laelectronics.enums.PaymentStatus;
import com.kasperovich.laelectronics.enums.ProductStatus;
import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.exception.PreconditionException;
import com.kasperovich.laelectronics.models.Discount;
import com.kasperovich.laelectronics.models.Edit;
import com.kasperovich.laelectronics.models.Order;
import com.kasperovich.laelectronics.models.Product;
import com.kasperovich.laelectronics.repository.OrderRepository;
import com.kasperovich.laelectronics.repository.PaymentRepository;
import com.kasperovich.laelectronics.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService{

    //TODO make unable to add deleted and OUT_OF_STOCK products

    OrderRepository orderRepository;

    PaymentRepository paymentRepository;
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll().stream().filter(x-> !x.getIsDeleted()).collect(Collectors.toList());
    }

    @Override
    public Order createOrder(@Valid Order order) throws PreconditionException {
        if(order.getProducts().stream().anyMatch(product -> product.getStatus().equals(ProductStatus.OUT_OF_STOCK)|| product.getIsDeleted())){
            throw new PreconditionException("Cannot create order with these products: Product could be deleted or out of stock");
        }
        if(order.getPayment().getStatus()==PaymentStatus.NOT_PAID){
            order.setOrderStatus(OrderStatus.NOT_STARTED);
        }
        else order.setOrderStatus(OrderStatus.IN_PROGRESS);
        if(order.getUser().getUserDiscount()!=null){
            order.setTotal(order.getTotal()-(order.getTotal()/100)*order.getUser().getUserDiscount().getDiscountPercent());
            order.getPayment().setAmount(order.getTotal());
        }
        order=orderRepository.save(order);
        paymentRepository.save(order.getPayment());
        return order;
    }

    @Override
    public Order updateOrder(Order order) {
        order.setEditData(new Edit(order.getEditData().getCreationDate(), new Timestamp(new Date().getTime())));
        Optional<Discount> userDiscount=Optional.ofNullable(order.getUser().getUserDiscount());
        if(userDiscount.isPresent()){
            order.setTotal(order.getTotal()-(order.getTotal()/100)*userDiscount.get().getDiscountPercent());
        }
        order.getPayment().setAmount(order.getTotal());
        order=orderRepository.save(order);
        if(order.getPayment()!=null){
            paymentRepository.save(order.getPayment());
        }
        return order;
    }

    @Override
    public Order deleteOrder(Long id) throws NotDeletableStatusException {
        Order order=orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if(order.getOrderStatus()==OrderStatus.DONE||order.getOrderStatus()==OrderStatus.CANCELLED){
            order.setIsDeleted(true);
            return orderRepository.save(order);
        }
        else throw new NotDeletableStatusException(
                "Unable to delete order with status "+order.getOrderStatus().toString()
        );
    }
}
