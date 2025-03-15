package eightplusone.bit.fit.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import eightplusone.bit.fit.domain.chat.enums.ChatCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageDto {
	private ChatCategory category;
	private String message;
	private String name;

	@JsonCreator
	public ChatMessageDto(@JsonProperty("category") ChatCategory category,
		@JsonProperty("message") String message) {
		this.category = category;
		this.message = message;
	}

	@JsonCreator
	public ChatMessageDto(ChatCategory category, String message, String name) {
		this.category = category;
		this.message = message;
		this.name = name;
	}
}
