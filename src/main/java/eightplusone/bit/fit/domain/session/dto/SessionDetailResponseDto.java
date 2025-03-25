package eightplusone.bit.fit.domain.session.dto;

import eightplusone.bit.fit.domain.session.entity.Session;
import eightplusone.bit.fit.domain.speaker.dto.SpeakerResponseDto;
import eightplusone.bit.fit.domain.tag.dto.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "SessionDetailResponseDto: 세션 상세 조회 Dto")
public class SessionDetailResponseDto {
	@Schema(description = "", example = "")
	private String title;

	@Schema(description = "", example = "")
	private String summary;

	private SpeakerResponseDto speaker;

	private TagDto tags;

	private Boolean isLiked;

	private Long likesCount;

	public static SessionDetailResponseDto from(Session session, SpeakerResponseDto speaker, TagDto tags,
		Boolean isLiked, Long likesCount) {
		return SessionDetailResponseDto.builder()
			.title(session.getTitle())
			.summary(session.getSummary())
			.isLiked(isLiked)
			.likesCount(likesCount)
			.speaker(speaker)
			.tags(tags)
			.build();
	}
}
