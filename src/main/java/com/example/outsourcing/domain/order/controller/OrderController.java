package com.example.outsourcing.domain.order.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.order.dto.request.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.request.OrderStatusChangeRequestDto;
import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderStatusChangeResponseDto;
import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.example.outsourcing.domain.order.exception.OrderErrorCode;
import com.example.outsourcing.domain.order.exception.OrderException;
import com.example.outsourcing.domain.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public CommonResponse<OrderResponseDto> saveOrder(
		@RequestAttribute(value = "userId", required = false) Long userId,
		@Valid @RequestBody OrderRequestDto orderRequestDto
	) {

		if (userId == null) {
			throw new OrderException(OrderErrorCode.UNAUTHORIZED_USER);
		}

		OrderStatus status = OrderStatus.REQUESTED;

		OrderResponseDto responseDto = orderService.createOrder(
			userId,
			orderRequestDto.getStoreId(),
			orderRequestDto.getMenuId(),
			status
		);

		return CommonResponse.created(responseDto);
	}

	@PutMapping("/status")
	public CommonResponse<OrderStatusChangeResponseDto> updateOrderStatus(
		@RequestAttribute(value = "userId", required = false) Long userId,
		@RequestBody OrderStatusChangeRequestDto orderStatusChangeRequestDto
	) {

		if (userId == null) {
			throw new OrderException(OrderErrorCode.UNAUTHORIZED_USER);
		}

		Long orderId = orderStatusChangeRequestDto.getOrderId();

		OrderStatusChangeResponseDto responseDto = orderService.updateOrderStatus(orderId, userId);
		return CommonResponse.ok(responseDto);
	}

	@DeleteMapping("/{orderId}")
	public CommonResponse<Void> deleteOrder(
		@RequestAttribute(value = "userId", required = false) Long userId,
		@PathVariable Long orderId
	) {
		if (userId == null) {
			throw new OrderException(OrderErrorCode.UNAUTHORIZED_USER);
		}

		orderService.deleteOrder(orderId, userId);
		return CommonResponse.ok(null);
	}

}

