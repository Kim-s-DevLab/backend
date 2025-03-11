package eightplusone.bit.fit.domain.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import eightplusone.bit.fit.domain.chat.enums.ChatCategory;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash(value = "chat_message")
@Getter
@NoArgsConstructor
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String messageId; // 메시지 PK

	private String sessionId; // 강연 ID

	private String userId; // 사용자 ID

	@Enumerated(EnumType.STRING)
	private ChatCategory category; // ENUM 사용

	@Column(length = 300, nullable = false)
	private String message; // 채팅 내용

	private String timestamp; // 작성 시간

	@Builder
	public ChatMessage(String sessionId, String userId, ChatCategory category, String message) {
		this.sessionId = sessionId;
		this.userId = userId;
		this.category = category;
		this.message = message;
		this.timestamp = LocalDateTime.now().toString();
	}
}
