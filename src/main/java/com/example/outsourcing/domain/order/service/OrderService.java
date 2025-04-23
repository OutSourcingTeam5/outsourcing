package com.example.outsourcing.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.entity.Order;
import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.example.outsourcing.domain.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderResponseDto createOrder(Long userId, Long storeId, Long menuId, OrderStatus status) {
		// 주문 생성
		Order order = new Order(status, userId, storeId, menuId);

		// 저장
		Order savedOrder = orderRepository.save(order);

		// 응답 DTO 생성
		return new OrderResponseDto(
			savedOrder.getId(),
			savedOrder.getUserId(),
			savedOrder.getStoreId(),
			savedOrder.getMenuId(),
			savedOrder.getOrderStatus(),
			savedOrder.getCreatedAt()
		);
	}
}
