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
	@Schema(description = "세션 제목", example = "디지털 송금 혁명의 미래")
	private String title;

	@Schema(description = "세션 요약", example = "차세대 디지털 송금 시스템과 국제 간 송금 혁신 방안 논의")
	private String summary;

	@Schema(description = "연사 정보")
	private SpeakerResponseDto speaker;

	@Schema(description = "태그 정보")
	private TagDto tags;

	@Schema(description = "좋아요 여부", example = "true")
	private Boolean isLiked;

	@Schema(description = "좋아요 개수", example = "10")
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
