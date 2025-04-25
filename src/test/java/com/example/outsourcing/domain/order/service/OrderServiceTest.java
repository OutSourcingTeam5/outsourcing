package com.example.outsourcing.domain.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.order.dto.response.OrderStatusChangeResponseDto;
import com.example.outsourcing.domain.order.entity.Order;
import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.example.outsourcing.domain.order.exception.OrderErrorCode;
import com.example.outsourcing.domain.order.exception.OrderException;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;  // 테스트 대상

	@Mock
	private OrderRepository orderRepository;  // Mock 객체로 처리할 repository들

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private UserRepository userRepository;

	private User user;
	private Store store;
	private Order order;

	@BeforeEach
	public void setUp() {
		// User 객체 mock 생성
		user = mock(User.class);
		when(user.getId()).thenReturn(1L);

		// Store 객체 생성
		store = new Store("Test Store", "09:00", "18:00", 1000, StoreStatus.OPEN, Category.KOREAN, user);

		// Menu 객체 생성
		Menu menu = new Menu(store, "Test Menu", 100, "Delicious menu");

		// Order 객체 생성 (주문 상태 REQUESTED)
		order = new Order(1L, OrderStatus.REQUESTED, user, store, menu);
	}

	@Test
	public void 주문_상태_변경_성공() {
		// Given
		Long orderId = order.getId();
		Long userId = user.getId();

		// 초기 상태 확인
		System.out.println("초기 상태: " + order.getOrderStatus());  // 초기 상태 출력 (REQUESTED)

		// mock 설정
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		when(storeRepository.findById(order.getStore().getId())).thenReturn(Optional.of(store));

		// When
		OrderStatusChangeResponseDto response = orderService.updateOrderStatus(orderId, userId);

		// Then
		assertNotNull(response);
		assertEquals(orderId, response.getOrderId());
		assertEquals(OrderStatus.COOKING, response.getOrderStatus());
		verify(orderRepository, times(1)).save(order);

		System.out.println("현재 상태: " + order.getOrderStatus());
	}

	@Test
	public void 주문_삭제_성공() {
		// Given
		Long orderId = order.getId();
		Long userId = user.getId(); // 주문자와 가게 소유자가 동일한 경우

		// mock 설정
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));  // 주문 존재
		when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));  // 가게 존재

		// When
		orderService.deleteOrder(orderId, userId);  // delete 호출

		// Then
		verify(orderRepository, times(1)).delete(order);  // delete가 한 번 호출되는지 확인

		// 삭제 후 조회 시, 주문이 없으면 정상 삭제된 것
		when(orderRepository.findById(orderId)).thenReturn(Optional.empty());  // 삭제된 주문 조회 시 empty 반환
		Optional<Order> deletedOrder = orderRepository.findById(orderId);
		System.out.println("========== 삭제 후 조회 결과: " + deletedOrder + " ==========");

		// 삭제된 후에는 Optional.empty()이어야 함
		assertTrue(deletedOrder.isEmpty(), "Order should be deleted.");

		// assertTrue(deletedOrder.isPresent(), "이건 실패해야 정상입니다");
	}

	@Test
	public void 권한_없는_사용자가_삭제_요청시_예외발생() {
		// Given
		Long orderId = order.getId();
		Long userId = 2L;  // 권한이 없는 다른 사용자

		// mock 설정
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

		// When / Then
		OrderException thrown = assertThrows(OrderException.class, () -> {
			orderService.deleteOrder(orderId, userId);
		});

		assertEquals(OrderErrorCode.NO_DELETE_AUTHORITY, thrown.getBaseCode());  // 권한 오류 확인
	}

}
