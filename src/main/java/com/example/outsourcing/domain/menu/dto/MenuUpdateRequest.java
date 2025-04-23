package com.example.outsourcing.domain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateRequest {

	@NotBlank(message = "메뉴 이름은 필수입니다.")
	private String name;

	@Positive(message = "가격은 0보다 커야 합니다.")
	private int price;

	private String description;
}
