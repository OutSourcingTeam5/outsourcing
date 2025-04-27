package com.example.outsourcing.domain.redis.service;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, String> redisTemplate;
	private static final String TODAY_HOT_KEYWORDS = "today_hot_keywords";
	private static final long TTL = 60 * 60 * 3; // 3시간

	/**
	 * 검색어 등록
	 * - 사용자들이 검색 시, 검색어를 redis 저장소에 등록하고 count를 센다.
	 * @author 조아현
	 * @Param keyword
	 * @since 2025 04 27
	 */
	public void addKeyword(String keyword) {

		redisTemplate.opsForZSet().incrementScore(TODAY_HOT_KEYWORDS, keyword, 1);

		Long ttl = redisTemplate.getExpire(TODAY_HOT_KEYWORDS);

		if (ttl == null || ttl == -1) {
			redisTemplate.expire(TODAY_HOT_KEYWORDS, Duration.ofSeconds(TTL));
		}
	}

	/**
	 * 인기 검색어 랭킹 조회
	 * - reids에 저장된 검색어 목록들 중 1-10 순위 내 검색어 목록을 반환한다.
	 * @author 조아현
	 * @Return List<String>
	 * @since 2025 04 27
	 */
	public List<String> getRankingKeyword() {
		return redisTemplate.opsForZSet().reverseRange(TODAY_HOT_KEYWORDS, 0, 9)
			.stream().toList();
	}

}
