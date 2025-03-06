package eightplusone.bit.fit.domain.session.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import eightplusone.bit.fit.domain.session.entity.Session;
import eightplusone.bit.fit.domain.session.entity.enums.CongestionLevel;
import eightplusone.bit.fit.domain.session.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final SessionRepository sessionRepository;

	// TODO: User ID 매개변수 부분 -> 토큰으로 수정
	// 체크인 시 레디스에 저장
	public void checkIn(Long userId) {
		redisTemplate.opsForHash().put("session_user", userId, "null");
	}

	// TODO: User ID 매개변수 부분 -> 토큰으로 수정
	// 체크아웃 시 레디스에서 삭제
	public void checkOut(Long userId) {
		redisTemplate.opsForHash().delete("session_user", userId);
	}

	public double getCongestionPercent(Long sessionId) {
		Session session = sessionRepository.findById(sessionId)
			.orElseThrow(() -> new EntityNotFoundException(sessionId + "에 해당하는 세션을 찾을 수 없습니다."));

		long connectedUsers = redisTemplate.opsForHash()
			.entries("session_user")
			.values()
			.stream()
			.filter(sessionId::equals)
			.count();

		Integer standardCnt = session.getStandardCount();

		return ((double)connectedUsers / standardCnt) * 100;
	}

	public String getCongestionLevel(double percent) {
		return CongestionLevel.fromPercent(percent);
	}
}
