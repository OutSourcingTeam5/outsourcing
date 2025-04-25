package com.example.outsourcing.domain.review.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ReviewSaveResponseDto {

	private final Long orderId;

	private final Long reviewId;

	private final String content;

	private final Integer rating;

	private final LocalDateTime updatedAt;

	public ReviewSaveResponseDto(Long orderId, Long reviewId, String content, Integer rating, LocalDateTime updatedAt) {
		this.orderId = orderId;
		this.reviewId = reviewId;
		this.content = content;
		this.rating = rating;
		this.updatedAt = updatedAt;
	}
}
