package com.kasperovich.laelectronics.api.dto.payment;

import com.kasperovich.laelectronics.enums.PaymentProviders;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE, makeFinal = false)
public class PaymentCreateDto {

    PaymentProviders provider;

    Long amount;

}
