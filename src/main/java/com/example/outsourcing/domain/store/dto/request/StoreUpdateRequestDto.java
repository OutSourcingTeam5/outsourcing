package com.example.outsourcing.domain.store.dto.request;

import org.hibernate.validator.constraints.Length;

import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.EnumCategory;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateRequestDto {

	@Length(max = 20, message = "가게명은 20자 이내로 작성해주세요. ")
	private String name;

	@Pattern(regexp = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$", message = "시간은 00:00 형식으로 입력해야 하며, 24시간제입니다.")
	private String openTime;

	@Pattern(regexp = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$", message = "시간은 00:00 형식으로 입력해야 하며, 24시간제입니다.")
	private String closeTime;

	private Integer minOrderPrice;

	@EnumCategory(target = Category.class, message = "올바른 카테고리를 작성해주세요.")
	private String category;

}
