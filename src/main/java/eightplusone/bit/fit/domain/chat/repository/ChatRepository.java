package eightplusone.bit.fit.domain.chat.repository;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import eightplusone.bit.fit.domain.chat.entity.ChatMessage;

@Repository
public class ChatRepository {
	private static final String CHAT_LIST_KEY = "chatMessages";
	private static final String LIKES_KEY = "chatLikes";
	private static final int MAX_CHAT_SIZE = 1000;

	private final RedisTemplate<String, Object> redisTemplate;

	public ChatRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	// 채팅 메시지 저장
	public void saveMessage(ChatMessage message) {
		redisTemplate.opsForList().rightPush(CHAT_LIST_KEY, message);
		redisTemplate.expire(CHAT_LIST_KEY, Duration.ofHours(2)); // 2시간 후 만료

		// 메시지 1000개 유지
		if (redisTemplate.opsForList().size(CHAT_LIST_KEY) > MAX_CHAT_SIZE) {
			redisTemplate.opsForList().leftPop(CHAT_LIST_KEY);
		}
	}

	// 최근 채팅 메시지 조회
	public List<Object> getRecentMessages() {
		return redisTemplate.opsForList().range(CHAT_LIST_KEY, 0, -1);
	}

	// 전체 데이터 삭제
	public void clearChat() {
		redisTemplate.delete(CHAT_LIST_KEY);
		redisTemplate.delete(LIKES_KEY);
	}
}
