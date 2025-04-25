package com.example.outsourcing.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderStatusChangeRequestDto {

	private final Long orderId;

	public OrderStatusChangeRequestDto(Long orderId) {
		this.orderId = orderId;
	}
}
