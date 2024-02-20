package com.kasperovich.laelectronics.api.dto.manufacturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationGetDto {

    String name;

    Long subNumber;

}
