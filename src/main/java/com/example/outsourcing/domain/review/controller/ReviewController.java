package com.example.outsourcing.domain.review.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.domain.review.dto.response.ReviewSaveResponseDto;
import com.example.outsourcing.domain.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * Review 생성
	 * - 배달 완료된 주문의 주문자만 리뷰를 생성할 수 있다.
	 * @author 조아현
	 * @Param userId
	 * @Param ReviewRequestDto
	 * @since 2025 04 26
	 */
	@PostMapping
	public CommonResponse<ReviewSaveResponseDto> saveReview(@RequestAttribute("userId") Long userId,
		@Valid @RequestBody ReviewRequestDto dto) {

		return CommonResponse.created(reviewService.saveReview(userId, dto));
	}

}
