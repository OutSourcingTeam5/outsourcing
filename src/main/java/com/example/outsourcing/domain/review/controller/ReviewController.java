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

	// - 리뷰 생성
	// - 고객은 주문 건에 대해 리뷰를 작성할 수 있습니다.
	// 	- 리뷰는 별점을 부여합니다.(1~5점)
	//
	// **예외처리**
	//
	// 	- `배달 완료` 되지 않은 주문은 리뷰를 작성할 수 없습니다.
	//
	// 	- 리뷰 조회
	// - 리뷰는 단건 조회할 수 없습니다.
	// - 리뷰는 가게 정보를 기준으로 다건 조회 가능하며, 최신순으로 정렬합니다.
	// 	- 리뷰를 별점 범위에 따라 조회할 수 있습니다.
	//     - ex) 3~5점

	//리뷰생성자는 배달 완료된 주문자여야 한다.

	@PostMapping
	public CommonResponse<ReviewSaveResponseDto> saveReview(@RequestAttribute("userId") Long userId,
		@Valid @RequestBody ReviewRequestDto dto) {

		return CommonResponse.created(reviewService.saveReview(userId, dto));
	}

}
