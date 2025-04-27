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

	public void addKeyword(String keyword) {

		redisTemplate.opsForZSet().incrementScore(TODAY_HOT_KEYWORDS, keyword, 1);

		Long ttl = redisTemplate.getExpire(TODAY_HOT_KEYWORDS);

		if (ttl == null || ttl == -1) {
			redisTemplate.expire(TODAY_HOT_KEYWORDS, Duration.ofSeconds(TTL));
		}
	}

	public List<String> getRankingKeyword() {
		return redisTemplate.opsForZSet().reverseRange(TODAY_HOT_KEYWORDS, 0, 9)
			.stream().toList();
	}

}
