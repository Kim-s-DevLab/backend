package eightplusone.bit.fit.domain.chat.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eightplusone.bit.fit.domain.chat.dto.ChatMessageDto;
import eightplusone.bit.fit.domain.chat.entity.ChatMessage;
import eightplusone.bit.fit.domain.chat.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatService {
	private final ChatRepository chatRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	public ChatService(ChatRepository chatRepository, RedisTemplate<String, Object> redisTemplate) {
		this.chatRepository = chatRepository;
		this.redisTemplate = redisTemplate;
	}

	// 메시지 전송 (Redis Pub/Sub 사용)
	public void sendMessage(ChatMessageDto dto, String userId, String sessionId) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		ChatMessage message = ChatMessage.builder()
			.sessionId(sessionId)
			.userId(userId)
			.category(dto.getCategory())
			.message(dto.getMessage())
			.build();

		chatRepository.saveMessage(message);

		String jsonMessage = objectMapper.writeValueAsString(message);
		String redisKey = "chat-" + sessionId;

		log.info("Redis 발행 메세지 : {}->{}", redisKey, jsonMessage);

		redisTemplate.convertAndSend(redisKey, dto);
	}

	// 최근 메시지 조회
	public List<Object> getRecentMessages() {
		return chatRepository.getRecentMessages();
	}

	// 강연 종료 후 데이터 삭제
	public void clearChat() {
		chatRepository.clearChat();
	}
}
