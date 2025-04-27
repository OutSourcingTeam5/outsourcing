package com.example.outsourcing.domain.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.search.service.SearchService;
import com.example.outsourcing.domain.store.dto.response.SliceResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@GetMapping
	public CommonResponse<SliceResponseDto<StoreResponseDto>> searchKeyword(@RequestAttribute("userId") Long userId,
		@RequestParam("keyword") String keyword, @RequestParam(value = "page", defaultValue = "1") int page) {

		return CommonResponse.ok(searchService.searchKeyword(userId, keyword, page));
	}

}
