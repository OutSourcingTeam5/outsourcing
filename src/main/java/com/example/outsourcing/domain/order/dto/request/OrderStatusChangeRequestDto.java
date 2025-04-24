package com.example.outsourcing.domain.order.dto.request;

import com.example.outsourcing.domain.order.entity.OrderStatus;

import lombok.Getter;

@Getter
public class OrderStatusChangeRequestDto {

	private final Long orderId;
	private final OrderStatus orderStatus;

	public OrderStatusChangeRequestDto(Long orderId, OrderStatus orderStatus) {
		this.orderId = orderId;
		this.orderStatus = orderStatus;
	}
}
