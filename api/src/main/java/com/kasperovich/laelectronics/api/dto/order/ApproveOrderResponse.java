package com.kasperovich.laelectronics.api.dto.order;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@Builder
public class ApproveOrderResponse {

    String status;

    String id;

}
