package com.example.outsourcing.domain.order.service;

import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderStatusChangeResponseDto;
import com.example.outsourcing.domain.order.entity.Order;
import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.example.outsourcing.domain.order.exception.OrderErrorCode;
import com.example.outsourcing.domain.order.exception.OrderException;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;

	@Transactional
	public OrderResponseDto createOrder(Long userId, Long storeId, Long menuId, OrderStatus status) {
		User user = getUserOrThrow(userId);
		Store store = getStoreOrThrow(storeId);
		Menu menu = getMenuOrThrow(menuId);

		if (!menu.getStore().getId().equals(storeId)) {
			throw new OrderException(OrderErrorCode.MENU_STORE_MISMATCH);
		}

		if (store.getUser().getId().equals(userId)) {
			throw new OrderException(OrderErrorCode.ORDER_FROM_OWNER_NOT_ALLOWED);
		}

		if (!isStoreOpen(store)) {
			throw new OrderException(OrderErrorCode.STORE_CLOSED);
		}

		Order order = new Order(status, user, store, menu);
		Order savedOrder = orderRepository.save(order);

		return new OrderResponseDto(
			savedOrder.getId(),
			savedOrder.getUser().getId(),
			savedOrder.getStore().getId(),
			savedOrder.getMenu().getId(),
			savedOrder.getOrderStatus(),
			savedOrder.getCreatedAt()
		);
	}

	@Transactional
	public OrderStatusChangeResponseDto updateOrderStatus(Long orderId, Long userId) {
		Order order = getOrderOrThrow(orderId);
		Store store = getStoreOrThrow(order.getStore().getId());

		if (!store.getUser().getId().equals(userId)) {
			throw new OrderException(OrderErrorCode.NOT_OWNER_OF_STORE);
		}

		OrderStatus newStatus = getNextStatus(order.getOrderStatus());
		order.setOrderStatus(newStatus);
		orderRepository.save(order);

		return new OrderStatusChangeResponseDto(
			order.getId(),
			order.getUser().getId(),
			order.getStore().getId(),
			order.getMenu().getId(),
			order.getOrderStatus(),
			order.getCreatedAt(),
			order.getUpdatedAt()
		);
	}

	@Transactional
	public void deleteOrder(Long orderId, Long userId) {
		Order order = getOrderOrThrow(orderId);
		Store store = getStoreOrThrow(order.getStore().getId());

		boolean isOwner = store.getUser().getId().equals(userId);
		boolean isOrderer = order.getUser().getId().equals(userId);

		if (!isOwner && !isOrderer) {
			throw new OrderException(OrderErrorCode.NO_DELETE_AUTHORITY);
		}

		if (order.getOrderStatus() == OrderStatus.COMPLETED) {
			throw new OrderException(OrderErrorCode.CANNOT_DELETE_COMPLETED_ORDER);
		}

		orderRepository.delete(order);
	}

	private User getUserOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new OrderException(OrderErrorCode.UNAUTHORIZED_USER));
	}

	private Store getStoreOrThrow(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new OrderException(OrderErrorCode.STORE_NOT_FOUND));
	}

	private Menu getMenuOrThrow(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new OrderException(OrderErrorCode.MENU_NOT_FOUND));
	}

	private Order getOrderOrThrow(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
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

		if (close.isBefore(open)) {
			return now.isAfter(open) || now.isBefore(close);
		}
		return !now.isBefore(open) && !now.isAfter(close);
	}
}