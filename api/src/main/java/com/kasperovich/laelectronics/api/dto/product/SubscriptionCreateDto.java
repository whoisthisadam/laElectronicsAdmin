package com.kasperovich.laelectronics.api.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kasperovich.laelectronics.api.dto.category.CategoryDto;
import com.kasperovich.laelectronics.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@EqualsAndHashCode(callSuper=false)
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class SubscriptionCreateDto {

    CategoryDto category;

    String name;

    Long price;

    Boolean isAvailable;

    String manufacturerName;

}
