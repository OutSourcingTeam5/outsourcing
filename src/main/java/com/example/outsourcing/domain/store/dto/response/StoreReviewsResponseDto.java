package com.example.outsourcing.domain.store.dto.response;

import java.util.List;

import com.example.outsourcing.domain.review.dto.response.ReviewResponseDto;

import lombok.Getter;

@Getter
public class StoreReviewsResponseDto {

	private final Long storeId;

	private final List<ReviewResponseDto> reviews;

	public StoreReviewsResponseDto(Long storeId, List<ReviewResponseDto> reviews) {
		this.storeId = storeId;
		this.reviews = reviews;
	}
}
