package com.example.outsourcing.domain.order.dto.response;

import java.time.LocalDateTime;

import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class OrderResponseDto {
	private final Long id;
	private final Long userId;
	private final Long storeId;
	private final Long menuId;

	private final OrderStatus orderStatus;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	public OrderResponseDto(
		Long id,
		Long userId,
		Long storeId,
		Long menuId,
		OrderStatus orderStatus,
		LocalDateTime createdAt
	) {
		this.id = id;
		this.userId = userId;
		this.storeId = storeId;
		this.menuId = menuId;
		this.orderStatus = orderStatus;
		this.createdAt = createdAt;
	}
}
