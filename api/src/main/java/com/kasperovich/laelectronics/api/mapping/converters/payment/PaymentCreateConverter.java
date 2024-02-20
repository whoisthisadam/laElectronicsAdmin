package com.kasperovich.laelectronics.api.mapping.converters.payment;

import com.kasperovich.laelectronics.api.dto.payment.PaymentCreateDto;
import com.kasperovich.laelectronics.enums.PaymentStatus;
import com.kasperovich.laelectronics.models.Edit;
import com.kasperovich.laelectronics.models.Order;
import com.kasperovich.laelectronics.models.Payment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentCreateConverter {

    public Payment convert(PaymentCreateDto paymentCreateDto, Order order) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getSubscription().getPrice());
        payment.setProvider(paymentCreateDto.getProvider());
        payment.setStatus(PaymentStatus.IN_PROGRESS);
        payment.setEditData(new Edit(new Timestamp(new Date().getTime()), null));
        payment.setStatus(PaymentStatus.PAID);
        return payment;
    }

}
