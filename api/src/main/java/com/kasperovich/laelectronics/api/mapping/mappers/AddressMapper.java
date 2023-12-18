package com.kasperovich.laelectronics.api.mapping.mappers;

import com.kasperovich.laelectronics.api.dto.address.AddressCreateDto;
import com.kasperovich.laelectronics.models.Address;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AddressMapper {

    Address toEntity(AddressCreateDto dto);

    AddressCreateDto toDto(Address address);

}
