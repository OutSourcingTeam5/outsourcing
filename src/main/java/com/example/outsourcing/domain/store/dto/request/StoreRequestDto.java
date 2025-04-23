package com.example.outsourcing.domain.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreRequestDto {

    private final String name;

    private final LocalTime openTime;

    private final LocalTime closeTime;

    private final Integer minOrderPrice;

    private final String category;

}
