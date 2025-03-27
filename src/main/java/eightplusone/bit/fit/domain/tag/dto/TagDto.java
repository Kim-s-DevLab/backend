package eightplusone.bit.fit.domain.tag.dto;

import eightplusone.bit.fit.domain.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "TagDto: 태그 정보 응답 및 필터링에 사용되는 dto")
public class TagDto {
	@Schema(description = "중점 분야", example = "송금 / 결제")
	private String field;

	@Schema(description = "주제", example = "기술 혁신")
	private String topic;

	@Schema(description = "콘텐츠 유형", example = "기조 연설")
	private String type;

	@Schema(description = "등급", example = "초급")
	private String level;

	public static TagDto from(Tag tag) {
		return TagDto.builder()
			.field(tag.getField())
			.topic(tag.getTopic())
			.type(tag.getType())
			.level(tag.getLevel())
			.build();
	}
}
