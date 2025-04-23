package com.example.outsourcing.domain.test.dto.request;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class TestReqeustDto {

    @Length(max = 5, message = "이름은 5자 이내로 작성해주세요.")
    private final String name;

    @Length(max = 10, message ="내용은 10자 이내로 작성해주세요.")
    private final String content;


}
