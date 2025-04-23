package com.example.outsourcing.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderRequestDto {

	private final Long storeId;

	private final Long menuId;

	public OrderRequestDto(Long storeId, Long menuId) {
		this.storeId = storeId;
		this.menuId = menuId;
	}
}
