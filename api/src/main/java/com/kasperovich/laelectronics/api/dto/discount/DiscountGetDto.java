package com.kasperovich.laelectronics.api.dto.discount;

import com.kasperovich.laelectronics.enums.Discounts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE, makeFinal = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountGetDto {

    Long id;

    Discounts name;

    Integer percent;

}
