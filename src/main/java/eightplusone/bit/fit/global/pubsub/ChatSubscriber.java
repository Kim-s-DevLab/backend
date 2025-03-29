package eightplusone.bit.fit.global.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import eightplusone.bit.fit.domain.chat.dto.ChatMessageDto;
import eightplusone.bit.fit.domain.chat.entity.ChatMessage;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatSubscriber implements MessageListener {
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public ChatSubscriber(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String receivedMessage = new String(message.getBody(), StandardCharsets.UTF_8);
			String channelName = new String(message.getChannel(), StandardCharsets.UTF_8);

			log.info("Redis에서 메시지 수신: 채널={}, 메시지={}", channelName, receivedMessage);

			// ChatMessage 기준으로 역직렬화
			ChatMessage chatMessage = objectMapper.readValue(receivedMessage, ChatMessage.class);

			// 채널명에서 sessionId 추출 (chat-pub:3 → 3)
			String sessionId = channelName.replace("chat-pub:", "");
			String websocketDestination = "/sub/chat/" + sessionId;

			// 필요한 필드만 넣어서 ChatMessageDto 구성 (name, likes 생략 가능)
			ChatMessageDto dto = new ChatMessageDto(
				chatMessage.getMessageId(),
				chatMessage.getCategory(),
				chatMessage.getMessage(),
				null, // name - Redis 캐시에서 조회하거나 null
				chatMessage.getUserId(),
				chatMessage.getSessionId(),
				chatMessage.getTimestamp(),
				0 // likes - 조회 안 했으면 기본값 0
			);

			log.info("WebSocket으로 전송 시도: {} -> {}", websocketDestination, dto);

			messagingTemplate.convertAndSend(websocketDestination, dto);

			log.info("WebSocket으로 전송 완료: {}", websocketDestination);

		} catch (Exception e) {
			log.error("Redis 메시지 처리 중 오류", e);
		}
	}
}
