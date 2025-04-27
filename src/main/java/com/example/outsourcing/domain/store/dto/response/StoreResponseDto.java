package com.example.outsourcing.domain.store.dto.response;

import lombok.Getter;

@Getter
public class StoreResponseDto {

	private final Long id;

	private final String name;

	private final Integer minOrderPrice;

	public StoreResponseDto(Long id, String name, Integer minOrderPrice) {
		this.id = id;
		this.name = name;
		this.minOrderPrice = minOrderPrice;
	}
}
