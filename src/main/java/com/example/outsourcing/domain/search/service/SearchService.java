package com.example.outsourcing.domain.search.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.search.repository.SearchRepository;
import com.example.outsourcing.domain.store.dto.response.SliceResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.enums.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final SearchRepository searchRepository;

	@Transactional(readOnly = true)
	public SliceResponseDto<StoreResponseDto> searchKeyword(Long userId, String keyword, int page) {

		int adjustedPage = (page > 0) ? page - 1 : 0;
		Pageable pageable = PageRequest.of(adjustedPage, 10);

		String categoryKeyword = checkKeywordCategory(keyword);

		Slice<StoreResponseDto> foundStores = searchRepository.searchStores(keyword, categoryKeyword, pageable);

		return new SliceResponseDto<>(
			foundStores.getContent(),
			foundStores.getNumber(),
			foundStores.getSize(),
			foundStores.isFirst(),
			foundStores.isLast()
		);
	}

	private String checkKeywordCategory(String keyword) {
		for (Category category : Category.values()) {
			if (category.getValue().contains(keyword)) {
				return category.name();
			}
		}
		return keyword;
	}

}
