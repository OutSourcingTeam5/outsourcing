package com.example.outsourcing.domain.review.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.order.entity.Order;
import com.example.outsourcing.domain.order.entity.OrderStatus;
import com.example.outsourcing.domain.order.repository.OrderRepository;
import com.example.outsourcing.domain.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.domain.review.dto.response.ReviewResponseDto;
import com.example.outsourcing.domain.review.dto.response.ReviewSaveResponseDto;
import com.example.outsourcing.domain.review.entity.Review;
import com.example.outsourcing.domain.review.exception.ReviewErrorCode;
import com.example.outsourcing.domain.review.exception.ReviewException;
import com.example.outsourcing.domain.review.repository.ReviewRepository;
import com.example.outsourcing.domain.store.dto.response.StoreReviewsResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.exception.StoreErrorCode;
import com.example.outsourcing.domain.store.exception.StoreException;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;

	/**
	 * Review 생성
	 * - 배달 완료된 주문의 주문자만 리뷰를 생성할 수 있다.
	 * @author 조아현
	 * @Param userId
	 * @Param ReviewRequestDto
	 * @since 2025 04 26
	 */
	@Transactional
	public ReviewSaveResponseDto saveReview(Long userId, @Valid ReviewRequestDto dto) {

		User user = checkUser(userId);

		Order order = orderRepository.findById(dto.getOrderId())
			.orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_ORDER_NOT_FOUND));

		if (!order.getUser().getId().equals(userId)) {
			throw new ReviewException(ReviewErrorCode.REVIEW_USER_NOT_CUSTOMER);
		}

		if (!order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
			throw new ReviewException(ReviewErrorCode.REVIEW_NOT_ORDER_COMPLETE);
		}

		Review review = new Review(dto.getContent(), dto.getRating(), false, order);

		reviewRepository.save(review);

		return new ReviewSaveResponseDto(order.getId(), review.getId(), review.getContent(), review.getRating(),
			review.getUpdatedAt());

	}

	/**
	 * Review 목록 조회
	 * - 가게의 리뷰 목록을 조회할 수 있다.
	 * @author 조아현
	 * @Param userId
	 * @Param storeId
	 * @Param startRating
	 * @Param endRating
	 * @Param StoreReviewsResponsedDto
	 * @since 2025 04 26
	 */
	@Transactional(readOnly = false)
	public StoreReviewsResponseDto findReviews(Long userId, Long storeId, Integer startRating, Integer endRating) {

		if (startRating >= endRating) {
			throw new ReviewException(ReviewErrorCode.REVIEW_RATING_WRONG);
		}

		User user = checkUser(userId);

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

		List<Review> reviews = reviewRepository.findAllByStoreIdAndRatingRangeWithUser(storeId, startRating, endRating);

		List<ReviewResponseDto> responses = reviews.stream().map(review -> {
			return new ReviewResponseDto(review.getId(), review.getContent(), review.getRating(),
				review.getUpdatedAt());
		}).collect(
			Collectors.toList());

		return new StoreReviewsResponseDto(storeId, responses);

	}

	/**
	 * user 검증
	 * - 사용자의 로그인상태 등을 검증한다.
	 * @author 조아현
	 * @Param userId
	 * @Return User
	 * @since 2025 04 26
	 */
	private User checkUser(Long userId) {

		if (userId == null) {
			throw new StoreException(ReviewErrorCode.REVIEW_USER_NOT_LOGIN);
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_USER_NOT_FOUND));

		return user;
	}

}
