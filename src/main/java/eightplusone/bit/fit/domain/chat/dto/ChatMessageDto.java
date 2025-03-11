package eightplusone.bit.fit.domain.chat.dto;

import eightplusone.bit.fit.domain.chat.enums.ChatCategory;
import lombok.Data;

@Data
public class ChatMessageDto {
	// private Long userId;
	private ChatCategory category;
	private String message;
}
