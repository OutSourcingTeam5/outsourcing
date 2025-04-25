package com.example.outsourcing.domain.review.dto.request;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRatingDto {

	@NotNull(message = "리뷰 평점은 필수값입니다.")
	@Range(min = 1, max = 5, message = "평점은 1~5 사이로 작성해주세요.")
	private Integer startRating;

	@NotNull(message = "리뷰 평점은 필수값입니다.")
	@Range(min = 1, max = 5, message = "평점은 1~5 사이로 작성해주세요.")
	private Integer endRating;

}
