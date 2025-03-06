package eightplusone.bit.fit.domain.session.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

	private final RedisTemplate<String, Object> redisTemplate;

	// TODO: User ID 매개변수 부분 -> 토큰으로 수정
	// 체크인 시 레디스에 저장
	public void checkIn(Long userId) {
		redisTemplate.opsForSet().add("userId", userId);
	}

	// TODO: User ID 매개변수 부분 -> 토큰으로 수정
	// 체크아웃 시 레디스에서 삭제
	public void checkOut(Long userId) {
		redisTemplate.opsForSet().remove("userId", userId);
	}

	private String getCongestionLevel(Long currentCount, Integer maxCapacity) {

		return "";
	}
}
