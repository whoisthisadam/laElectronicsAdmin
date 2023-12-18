package com.kasperovich.laelectronics.api.dto.order;

import com.kasperovich.laelectronics.api.dto.payment.PaymentCreateDto;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class OrderUpdateDto {
    //TODO: make able to change order status via API

    List<Long>products;

    PaymentCreateDto payment;


}
