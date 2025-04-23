package com.example.outsourcing.domain.store.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Category {

    FAST_FOOD("패스트푸드"),
    BUNSIK("분식"),
    DESSERT("디저트"),
    CHICKEN("치킨"),
    KOREAN("한식"),
    STEW_SOUP("찜/탕"),
    CHINESE("중식"),
    PIZZA("피자"),
    JAPANESE("돈까스/회"),
    WESTERN("양식"),
    MEAT("고기"),
    DOSIRAK("도시락"),
    JOKBAL_BOSSAM("족발/보쌈"),
    ASIAN("아시안"),
    NIGHT_FOOD("야식");

    @JsonValue
    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Category verifyCategory(String value) {

        return Stream.of(Category.values())
                .filter(category -> category.value.equals(value))
                .findAny()
                .orElseThrow(()-> new RuntimeException("카테고리 잘못 입력"));
//                .orElse(null);
    }
}
