package eightplusone.bit.fit.domain.chat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import eightplusone.bit.fit.domain.chat.dto.ChatMessageDto;
import eightplusone.bit.fit.domain.chat.entity.ChatMessage;
import eightplusone.bit.fit.domain.chat.repository.ChatRepository;

@Service
public class ChatService {
	private final ChatRepository chatRepository;
	private final StringRedisTemplate redisTemplate;

	public ChatService(ChatRepository chatRepository, StringRedisTemplate redisTemplate) {
		this.chatRepository = chatRepository;
		this.redisTemplate = redisTemplate;
	}

	// 메시지 전송 (Redis Pub/Sub 사용)
	public void sendMessage(ChatMessageDto dto, Long userId) {
		ChatMessage message = new ChatMessage();
		message.setUserId(userId);
		message.setCategory(dto.getCategory());
		message.setMessage(dto.getMessage());
		message.setTimestamp(LocalDateTime.now());

		// DB 저장
		chatRepository.saveMessage(message);

		// "userId|category|message" 형식으로 변환하여 Redis에 전송
		String redisMessage = userId + "|" + dto.getCategory() + "|" + dto.getMessage();
		System.out.println("🔹 Redis로 전송할 메시지: " + redisMessage);
		redisTemplate.convertAndSend("chat-room", redisMessage);
		System.out.println("✅ Redis에 메시지 발행 완료");

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
