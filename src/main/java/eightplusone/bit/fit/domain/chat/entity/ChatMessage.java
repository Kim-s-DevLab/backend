package eightplusone.bit.fit.domain.chat.entity;

import eightplusone.bit.fit.domain.chat.enums.ChatCategory;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "chat_message")
@Getter
@NoArgsConstructor
public class ChatMessage {
	@Id
	private String messageId; // 메시지 PK

	private Long sessionId; // 강연 ID

	private String userId; // 사용자 ID

	@Enumerated(EnumType.STRING)
	private ChatCategory category; // ENUM 사용

	@Column(length = 300, nullable = false)
	private String message; // 채팅 내용

	private LocalDateTime timestamp; // 작성 시간

	@Builder
	public ChatMessage(String messageId, Long sessionId, String userId, ChatCategory category, String message,
		LocalDateTime timestamp) {
		this.messageId = messageId != null ? messageId : UUID.randomUUID().toString();
		this.sessionId = sessionId;
		this.userId = userId;
		this.category = category;
		this.message = message;
		this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
	}

}