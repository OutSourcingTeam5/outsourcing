package com.example.outsourcing.domain.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.redis.service.RedisService;
import com.example.outsourcing.domain.search.dto.response.SearchRankingDto;
import com.example.outsourcing.domain.search.repository.SearchRepository;
import com.example.outsourcing.domain.store.dto.response.SliceResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.enums.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final SearchRepository searchRepository;
	private final RedisService redisService;

	/**
	 * 통합 검색
	 * @author 조아현
	 * @since 2025 04 27
	 * @param   userId
	 * @param   keyword
	 * @param   page
	 * @return SliceResponseDto<StoreResponseDto>
	 */
	@Transactional(readOnly = true)
	public SliceResponseDto<StoreResponseDto> searchKeyword(Long userId, String keyword, int page) {

		int adjustedPage = (page > 0) ? page - 1 : 0;
		Pageable pageable = PageRequest.of(adjustedPage, 10);

		String categoryKeyword = checkKeywordCategory(keyword);

		Slice<StoreResponseDto> foundStores = searchRepository.searchStores(keyword, categoryKeyword, pageable);

		if (!foundStores.isEmpty() && keyword.length() > 1) {
			redisService.addKeyword(keyword);
		}

		return new SliceResponseDto<>(
			foundStores.getContent(),
			foundStores.getNumber(),
			foundStores.getSize(),
			foundStores.isFirst(),
			foundStores.isLast());
	}

	/**
	 * 인기 검색어 조회
	 * @author 조아현
	 * @since 2025 04 27
	 * @param   userId
	 * @return List<SearchRankingDto>
	 */
	@Transactional(readOnly = true)
	public List<SearchRankingDto> getRanking(Long userId) {

		List<String> keywords = redisService.getRankingKeyword();

		List<SearchRankingDto> rankingList = new ArrayList<>();

		for (int i = 0; i < keywords.size(); i++) {

			rankingList.add(new SearchRankingDto(i + 1, keywords.get(i)));

		}

		return rankingList;
	}

	/**
	 * 입력된 검색값이 카테고리 value에 포함되는지 확인하는 메서드
	 * @author 조아현
	 * @since 2025 04 27
	 * @param  keyword
	 * @return String
	 */
	private String checkKeywordCategory(String keyword) {
		for (Category category : Category.values()) {
			if (category.getValue().contains(keyword)) {
				return category.name();
			}
		}
		return keyword;
	}

}
