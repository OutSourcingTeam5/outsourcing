package com.example.outsourcing.domain.store.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class storeSaveResponseDto {

    private final Long id;

    private final String name;

    private final LocalTime openTime;

    private final LocalTime closeTime;

    private final Integer minOrderPrice;

    private final String status;

    private final String category;

}
