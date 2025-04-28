package com.example.outsourcing.domain.review.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.order.entity.Order;
import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.domain.review.dto.response.ReviewSaveResponseDto;
import com.example.outsourcing.domain.review.entity.Review;
import com.example.outsourcing.domain.review.repository.ReviewRepository;
import com.example.outsourcing.domain.store.dto.response.StoreReviewsResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.Role;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ReviewService reviewService;

	private User user;
	private Store store;
	private Order order;
	private Menu menu;
	private Review review;
	private ReviewRequestDto reviewRequestDto;

	@BeforeEach
	public void setUp() {

		user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		ReflectionTestUtils.setField(user, "id", 1L);

		store = new Store("칙스칙스", "01:00", "12:00", 15000, StoreStatus.OPEN, Category.WESTERN, user);
		ReflectionTestUtils.setField(store, "id", 1L);

		// Menu 객체 생성
		menu = new Menu(store, "Test Menu", 100, "Delicious menu");
		ReflectionTestUtils.setField(menu, "id", 1L);

		// Order 객체 생성 (주문 상태 REQUESTED)
		order = new Order(1L, OrderStatus.COMPLETED, user, store, menu);
		ReflectionTestUtils.setField(order, "id", 1L);

		review = new Review(
			"맛있어요", 5, false, order);
		ReflectionTestUtils.setField(review, "id", 1L);

		reviewRequestDto = new ReviewRequestDto(order.getId(), "내용", 5);

	}

	@Test
	@DisplayName("리뷰 생성 테스트")
	public void test_saveReview_success() {

		//given
		Long userId = user.getId();
		Long orderId = order.getId();

		given(userRepository.findById(userId)).willReturn(Optional.of(user));
		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

		//when
		ReviewSaveResponseDto result = reviewService.saveReview(userId, reviewRequestDto);

		//then
		assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
		assertNotNull(result);
		verify(reviewRepository, times(1)).save(any(Review.class));

	}

	@Test
	@DisplayName("리뷰 목록 조회 성공")
	public void test_findReviews_success() {

		//given
		Long userId = user.getId();
		Long storeId = store.getId();
		Integer startRating = 1;
		Integer endRating = 5;

		given(userRepository.findById(userId)).willReturn(Optional.of(user));
		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

		List<Review> reviews = List.of(
			new Review("맛있어요", 5, false, order),
			new Review("괜찮아요", 4, false, order)
		);

		StoreReviewsResponseDto dtoList = reviewService.findReviews(userId, storeId, 1, 5);

		assertNotNull(reviews);
		assertEquals(review.getContent(), reviews.get(0).getContent());
		verify(reviewRepository, times(1)).findAllByStoreIdAndRatingRangeWithUser(storeId, startRating, endRating);

	}

}