package com.example.outsourcing.domain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequest {

	@NotNull(message = "가게 ID는 필수입니다.")
	private Long storeId;


	@NotBlank(message = "메뉴 이름은 필수입니다.")
	private String name;


	@Positive(message = "가격은 0보다 커야 합니다.")
	private int price;


	private String description;
}
