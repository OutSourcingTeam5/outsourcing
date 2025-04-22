package com.example.outsourcing.domain.Store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalDateTime;
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
