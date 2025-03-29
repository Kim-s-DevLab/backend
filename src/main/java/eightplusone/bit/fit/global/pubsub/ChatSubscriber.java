package eightplusone.bit.fit.global.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import eightplusone.bit.fit.domain.chat.dto.ChatMessageDto;
import eightplusone.bit.fit.domain.chat.entity.ChatMessage;
import eightplusone.bit.fit.domain.user.repository.UserRedisRepository;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatSubscriber implements MessageListener {
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RedisTemplate<String, Object> redisTemplate;
	private final UserRedisRepository userRedisRepository;

	public ChatSubscriber(SimpMessagingTemplate messagingTemplate,
		RedisTemplate<String, Object> redisTemplate,
		UserRedisRepository userRedisRepository) {
		this.messagingTemplate = messagingTemplate;
		this.redisTemplate = redisTemplate;
		this.userRedisRepository = userRedisRepository;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String receivedMessage = new String(message.getBody(), StandardCharsets.UTF_8);
			String channelName = new String(message.getChannel(), StandardCharsets.UTF_8);

			log.info("Redis에서 메시지 수신: 채널={}, 메시지={}", channelName, receivedMessage);

			ChatMessage chatMessage = objectMapper.readValue(receivedMessage, ChatMessage.class);

			String sessionId = channelName.replace("chat-pub:", "");
			String websocketDestination = "/sub/chat/" + sessionId;

			// 이름 조회
			String userName = userRedisRepository.getUserName(chatMessage.getUserId());

			// 좋아요 수 조회
			String zsetKey = "questions:session:" + sessionId;
			Double score = redisTemplate.opsForZSet().score(zsetKey, chatMessage.getMessageId());
			int likeCount = score != null ? score.intValue() : 0;

			ChatMessageDto dto = new ChatMessageDto(
				chatMessage.getMessageId(),
				chatMessage.getCategory(),
				chatMessage.getMessage(),
				userName != null ? userName : "알 수 없음",
				chatMessage.getUserId(),
				chatMessage.getSessionId(),
				chatMessage.getTimestamp(),
				likeCount
			);

			log.info("WebSocket으로 전송 시도: {} -> {}", websocketDestination, dto);
			messagingTemplate.convertAndSend(websocketDestination, dto);
		} catch (Exception e) {
			log.error("Redis 메시지 처리 중 오류", e);
		}
	}
}