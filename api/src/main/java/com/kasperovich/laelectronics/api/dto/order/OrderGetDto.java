package com.kasperovich.laelectronics.api.dto.order;

import com.kasperovich.laelectronics.api.dto.payment.PaymentGetDto;
import com.kasperovich.laelectronics.api.dto.product.SubscriptionGetDto;
import com.kasperovich.laelectronics.enums.OrderStatus;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE, makeFinal = false)
public class OrderGetDto {

    Long id;

    String userEmail;

    Long userId;

    Long total;

    PaymentGetDto payment;

    OrderStatus status;

    List<SubscriptionGetDto> products;

}
