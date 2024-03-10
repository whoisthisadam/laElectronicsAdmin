package com.kasperovich.laelectronics.api.dto.payment;

import com.kasperovich.laelectronics.enums.PaymentProviders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE, makeFinal = false)
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDto {

    PaymentProviders provider;

    Long amount;

}
