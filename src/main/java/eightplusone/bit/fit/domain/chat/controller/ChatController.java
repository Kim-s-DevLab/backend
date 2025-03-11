package eightplusone.bit.fit.domain.chat.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import eightplusone.bit.fit.domain.chat.dto.ChatMessageDto;
import eightplusone.bit.fit.domain.chat.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {
	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	// Redis로 발행
	@MessageMapping("/chat/{sessionId}")
	public void sendMessage(@DestinationVariable("sessionId") String sessionId,
		@Payload ChatMessageDto message,
		@Header("User-Id") String userId) throws JsonProcessingException {
		chatService.sendMessage(message, userId, sessionId);
	}

	// 최근 채팅 메시지 조회 (REST API)
	@GetMapping("/recent")
	public List<Object> getRecentMessages() {
		return chatService.getRecentMessages();
	}

	// 강연 종료 후 데이터 삭제
	@DeleteMapping("/clear")
	public void clearChat() {
		chatService.clearChat();
	}
}
