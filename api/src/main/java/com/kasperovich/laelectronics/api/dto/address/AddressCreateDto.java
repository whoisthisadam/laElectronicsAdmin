package com.kasperovich.laelectronics.api.dto.address;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreateDto {

    String lineOne;

    String lineTwo;

    String city;

    String province;

    String postCode;

    String country;

}
