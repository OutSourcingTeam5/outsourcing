package com.example.outsourcing.domain.review.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

	private Long orderId;

	@NotBlank(message = "리뷰 내용은 필수값입니다.")
	@Length(max = 200, message = "리뷰 내용은 200자 이내로 작성해주세요.")
	private String content;

	@NotNull(message = "리뷰 평점은 필수값입니다.")
	@Range(min = 1, max = 5, message = "평점은 1~5 사이로 작성해주세요.")
	private Integer rating;

}
