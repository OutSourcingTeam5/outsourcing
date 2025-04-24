package com.example.outsourcing.domain.store.dto.response;

import java.time.LocalTime;

import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.Category;

import lombok.Getter;

@Getter
public class StoreUpdateResponseDto {

	private final Long id;

	private final String name;

	private final LocalTime openTime;

	private final LocalTime closeTime;

	private final Integer minOrderPrice;

	private final Category category;

	public StoreUpdateResponseDto(Store store) {
		this.id = store.getId();
		this.name = store.getName();
		this.openTime = store.getOpenTime();
		this.closeTime = store.getCloseTime();
		this.minOrderPrice = store.getMinOrderPrice();
		this.category = store.getCategory();
	}

}
