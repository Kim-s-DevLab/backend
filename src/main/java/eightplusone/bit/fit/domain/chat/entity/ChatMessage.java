package eightplusone.bit.fit.domain.chat.entity;

import java.time.LocalDateTime;

import eightplusone.bit.fit.domain.chat.enums.ChatCategory;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId; // 메시지 PK

	private Long lectureId; // 강연 ID

	private Long userId; // 사용자 ID

	@Enumerated(EnumType.STRING)
	private ChatCategory category; // ENUM 사용

	@Column(length = 300, nullable = false)
	private String message; // 채팅 내용

	private LocalDateTime timestamp; // 작성 시간
}
