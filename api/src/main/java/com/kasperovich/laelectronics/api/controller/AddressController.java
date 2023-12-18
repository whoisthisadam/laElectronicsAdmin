package com.kasperovich.laelectronics.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequestMapping("/data/address")
@RequiredArgsConstructor
@Tag(name = "Address")
@CacheConfig(cacheNames = "Address")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {





}
