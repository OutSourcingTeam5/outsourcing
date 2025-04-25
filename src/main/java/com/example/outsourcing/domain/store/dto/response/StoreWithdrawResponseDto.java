package com.example.outsourcing.domain.store.dto.response;

import com.example.outsourcing.domain.store.entity.Store;

import lombok.Getter;

@Getter
public class StoreWithdrawResponseDto {

	private final Long id;
	private final String name;
	private final String category;
	private final String message;

	public StoreWithdrawResponseDto(Store store, String message) {
		this.id = store.getId();
		this.name = store.getName();
		this.category = String.valueOf(store.getCategory());
		this.message = message;
	}
}
