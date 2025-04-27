package com.example.outsourcing.domain.search.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.search.dto.response.SearchRankingDto;
import com.example.outsourcing.domain.search.service.SearchService;
import com.example.outsourcing.domain.store.dto.response.SliceResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	/**
	 * 통합 검색
	 * @author 조아현
	 * @since 2025 04 27
	 * @param   userId
	 * @param   keyword
	 * @param   page
	 * @return CommonResponse<SliceResponseDto < StoreResponseDto>>
	 */
	@GetMapping
	public CommonResponse<SliceResponseDto<StoreResponseDto>> searchKeyword(@RequestAttribute("userId") Long userId,
		@RequestParam("keyword") String keyword, @RequestParam(value = "page", defaultValue = "1") int page) {

		return CommonResponse.ok(searchService.searchKeyword(userId, keyword, page));
	}

	/**
	 * 인기 검색어 조회
	 * @author 조아현
	 * @since 2025 04 27
	 * @param   userId
	 * @return CommonResponse<List < SearchRankingDto>>
	 */
	@GetMapping("/ranking")
	public CommonResponse<List<SearchRankingDto>> getRanking(@RequestAttribute("userId") Long userId) {

		return CommonResponse.ok(searchService.getRanking(userId));
	}

}
