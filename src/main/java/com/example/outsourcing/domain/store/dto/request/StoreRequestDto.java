package com.example.outsourcing.domain.store.dto.request;

import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.EnumCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {

    @NotBlank(message = "가게명은 필수값입니다.")
    @Length(max=20,message = "가게명은 20자 이내로 작성해주세요. ")
    private  String name;

    @Pattern(regexp = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$", message = "시간은 00:00 형식으로 입력해야 하며, 24시간제입니다.")
    private LocalTime openTime;

    @Pattern(regexp = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$", message = "시간은 00:00 형식으로 입력해야 하며, 24시간제입니다.")
    private LocalTime closeTime;

    @NotBlank(message = "최소주문금액은 필수값입니다.")
    private Integer minOrderPrice;

    @EnumCategory(target = Category.class ,message = "올바른 카테고리를 작성해주세요.")
    private String category;

}
