package com.example.outsourcing.domain.search.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchRankingDto {

	private final int rank;

	private final String keyword;

}
