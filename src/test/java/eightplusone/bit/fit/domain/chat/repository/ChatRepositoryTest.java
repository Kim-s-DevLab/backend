package eightplusone.bit.fit.domain.chat.repository;

import static org.junit.jupiter.api.Assertions.*;

import eightplusone.bit.fit.domain.chat.entity.ChatMessage;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ChatRepositoryTest {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private static final String SESSION_ID = "testSession";

	@BeforeEach
	void setUp() {
		// 테스트 전에 Redis에서 기존 데이터 삭제
		chatRepository.clearChat(SESSION_ID);
	}

	@Test
	void testChatMessageLimit() {
		// 1100개의 메시지를 저장하여 1000개만 유지되는지 테스트
		for (int i = 1; i <= 1100; i++) {
			ChatMessage message = ChatMessage.builder()
				.sessionId(SESSION_ID)
				.userId("user" + i)
				.message("Message " + i)
				.build();
			chatRepository.saveMessage(message);
		}

		// 저장된 메시지 개수 가져오기
		List<Object> messages = chatRepository.getRecentMessages(SESSION_ID);

		// 메시지가 1000개만 유지되는지 검증
		assertEquals(1000, messages.size());

		// 가장 오래된 메시지가 삭제되었는지 확인
		assertFalse(messages.contains("Message 1")); // 첫 번째 메시지가 삭제되었어야 함
	}

	@Test
	void testChatMessageExpiration() throws InterruptedException {
		// 메시지 저장
		ChatMessage message = ChatMessage.builder()
			.sessionId(SESSION_ID)
			.userId("user123")
			.message("Test TTL")
			.build();
		chatRepository.saveMessage(message);

		// TTL 설정 확인 (2시간 → 7200초)
		Long ttl = redisTemplate.getExpire("chat-" + SESSION_ID);
		assertNotNull(ttl);
		assertTrue(ttl > 7100); // 2시간(7200초) 설정되었는지 확인

		// TTL을 5초로 변경하여 삭제되는지 테스트
		redisTemplate.expire("chat-" + SESSION_ID, Duration.ofSeconds(5));

		// 6초 대기 후 데이터가 삭제되었는지 확인
		Thread.sleep(6000);
		assertFalse(chatRepository.existsBySessionId(SESSION_ID));
	}
}
