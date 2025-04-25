package com.example.outsourcing.domain.order.dto.response;

import java.time.LocalDateTime;

import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class OrderStatusChangeResponseDto {
	private final Long orderId;
	private final Long userId;
	private final Long storeId;
	private final Long menuId;

	private final OrderStatus orderStatus;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime updatedAt;

	public OrderStatusChangeResponseDto(
		Long orderId,
		Long userId,
		Long storeId,
		Long menuId,
		OrderStatus orderStatus,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		this.orderId = orderId;
		this.userId = userId;
		this.storeId = storeId;
		this.menuId = menuId;
		this.orderStatus = orderStatus;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
