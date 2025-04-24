package com.example.outsourcing.domain.order.service;

import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderStatusChangeResponseDto;
import com.example.outsourcing.domain.order.entity.Order;
import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.example.outsourcing.domain.order.exception.OrderErrorCode;
import com.example.outsourcing.domain.order.exception.OrderException;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	// private final MenuRepository menuRepository;

	@Transactional
	public OrderResponseDto createOrder(Long userId, Long storeId, Long menuId, OrderStatus status) {
		// 가게 존재 확인
		if (!storeRepository.existsById(storeId)) {
			throw new OrderException(OrderErrorCode.STORE_NOT_FOUND);
		}
		//
		// // 메뉴 존재 확인
		// if (!menuRepository.existsById(menuId)) {
		// 	throw new OrderException(OrderErrorCode.MENU_NOT_FOUND);
		// }

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new OrderException(OrderErrorCode.STORE_NOT_FOUND));

		// 주문 시도한 userId와 가게 Owner가 같다면 예외 발생
		if (store.getUser().getId().equals(userId)) {
			throw new OrderException(OrderErrorCode.ORDER_FROM_OWNER_NOT_ALLOWED);
		}

		// ✨ 내가 만든 메서드로 열려있는지 체크
		if (!isStoreOpen(store)) {
			throw new OrderException(OrderErrorCode.STORE_CLOSED);
		}

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

	@Transactional
	public OrderStatusChangeResponseDto updateOrderStatus(Long orderId, Long userId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

		Store store = storeRepository.findById(order.getStoreId())
			.orElseThrow(() -> new OrderException(OrderErrorCode.STORE_NOT_FOUND));

		// 로그인한 유저가 이 가게의 OWNER인지 확인
		if (!store.getUser().getId().equals(userId)) {
			throw new OrderException(OrderErrorCode.NOT_OWNER_OF_STORE);
		}

		// 상태 업데이트
		OrderStatus newStatus = getNextStatus(order.getOrderStatus());
		order.setOrderStatus(newStatus);

		// 주문 저장 (상태 변경이 반영되며, `updatedAt`은 자동으로 갱신됨)
		orderRepository.save(order);

		// 상태 변경 후 응답 DTO 반환
		return new OrderStatusChangeResponseDto(
			order.getId(),
			order.getUserId(),
			order.getStoreId(),
			order.getMenuId(),
			order.getOrderStatus(),
			order.getCreatedAt(),
			order.getUpdatedAt()  // 상태 변경 후 업데이트된 시간
		);
	}

	@Transactional
	public void deleteOrder(Long orderId, Long userId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

		Store store = storeRepository.findById(order.getStoreId())
			.orElseThrow(() -> new OrderException(OrderErrorCode.STORE_NOT_FOUND));

		boolean isOwner = store.getUser().getId().equals(userId);
		boolean isOrderer = order.getUserId().equals(userId);

		if (!isOwner && !isOrderer) {
			throw new OrderException(OrderErrorCode.NO_DELETE_AUTHORITY);
		}

		if (order.getOrderStatus() == OrderStatus.COMPLETED) {
			throw new OrderException(OrderErrorCode.CANNOT_DELETE_COMPLETED_ORDER);
		}

		orderRepository.delete(order);
	}

	private OrderStatus getNextStatus(OrderStatus currentStatus) {
		return switch (currentStatus) {
			case REQUESTED -> OrderStatus.COOKING;
			case COOKING -> OrderStatus.COMPLETED;
			case COMPLETED -> throw new OrderException(OrderErrorCode.ALREADY_COMPLETED);
		};
	}

	private boolean isStoreOpen(Store store) {
		LocalTime now = LocalTime.now();
		LocalTime open = store.getOpenTime();
		LocalTime close = store.getCloseTime();

		// 밤 장사 고려 (ex. 22:00 ~ 03:00)
		if (close.isBefore(open)) {
			return now.isAfter(open) || now.isBefore(close);
		}
		return !now.isBefore(open) && !now.isAfter(close);
	}
}
