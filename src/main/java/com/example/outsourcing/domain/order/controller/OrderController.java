package com.example.outsourcing.domain.order.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.order.dto.request.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.entity.OrderStatus;
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
		// @RequestAttribute("userId") Long userId,
		@Valid @RequestBody OrderRequestDto orderRequestDto
	) {
		Long userId = 2L; // 추후 JWT에서 추출할 예정
		OrderStatus status = OrderStatus.REQUESTED;

		OrderResponseDto responseDto = orderService.createOrder(
			userId,
			orderRequestDto.getStoreId(),
			orderRequestDto.getMenuId(),
			status
		);

		return CommonResponse.created(responseDto);
	}

}

