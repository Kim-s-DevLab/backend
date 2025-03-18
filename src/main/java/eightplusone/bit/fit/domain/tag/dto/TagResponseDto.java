package eightplusone.bit.fit.domain.tag.dto;

import eightplusone.bit.fit.domain.tag.entity.Tag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagResponseDto {
	private String field;

	private String topic;

	private String type;

	private String level;

	public static TagResponseDto from(Tag tag) {
		return TagResponseDto.builder()
			.field(tag.getField())
			.topic(tag.getTopic())
			.type(tag.getType())
			.level(tag.getLevel())
			.build();
	}
}
