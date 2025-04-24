package com.example.outsourcing.domain.store.dto.response;

import lombok.Getter;

@Getter
public class StoreResponseDto {

	private final String name;

	private final Integer minOrderPrice;

	public StoreResponseDto(String name, Integer minOrderPrice) {
		this.name = name;
		this.minOrderPrice = minOrderPrice;
	}
}
