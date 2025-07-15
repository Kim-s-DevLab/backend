package eightplusone.bit.fit.global.pubsub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eightplusone.bit.fit.domain.chat.dto.ChatMessageDto;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatSubscriber implements MessageListener {
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;

	public ChatSubscriber(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
		this.messagingTemplate = messagingTemplate;
		this.objectMapper = objectMapper;
	}

	@Override
	public void onMessage(@NonNull Message message, byte[] pattern) {
		try {
			String raw = new String(message.getBody(), StandardCharsets.UTF_8);
			String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
			log.info("Redis에서 메시지 수신: 채널={}, 메시지={}", channel, raw);

			// 채널 확인: chat-pub:로 시작하는 경우에만 처리
			if (!channel.startsWith("chat-pub:")) {
				log.info("❌ chat-pub 채널이 아니므로 무시: {}", channel);
				return;
			}

			// TOP3 처리
			if (channel.startsWith("chat-top3:")) {
				String sessionId = channel.substring("chat-top3:".length());
				List<ChatMessageDto> top3 = objectMapper.readValue(raw, new TypeReference<List<ChatMessageDto>>() {});
				messagingTemplate.convertAndSend("/sub/top3/" + sessionId, top3);
				log.info("✅ TOP3 메시지 WebSocket 전송 완료 (세션 {}): {}", sessionId, top3);
				return;
			}

			ChatMessageDto dto = objectMapper.readValue(raw, ChatMessageDto.class);

			if (dto.getMessageId() == null) {
				log.warn("❗ messageId가 null입니다. 메시지 무시: {}", raw);
				return;
			}

			messagingTemplate.convertAndSend("/sub/chat/" + dto.getSessionId(), dto);
			log.info("✅ 채팅 메시지 WebSocket 전송 완료: {}", dto);
		} catch (Exception e) {
			log.error("Redis 메시지 처리 중 오류", e);
		}
	}
}