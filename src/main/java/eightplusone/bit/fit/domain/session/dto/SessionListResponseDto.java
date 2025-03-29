package eightplusone.bit.fit.domain.session.dto;

import eightplusone.bit.fit.domain.session.entity.Session;
import eightplusone.bit.fit.domain.speaker.dto.SpeakerResponseDto;
import eightplusone.bit.fit.domain.tag.dto.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "SessionListResponseDto: 전체 세션 조회 Dto")
public class SessionListResponseDto {
	@Schema(description = "세션 아이디", example = "1")
	private Long id;

	@Schema(description = "세션 제목", example = "디지털 송금 혁명의 미래")
	private String title;

	@Schema(description = "세션 요약", example = "차세대 디지털 송금 시스템과 국제 간 송금 혁신 방안 논의")
	private String summary;

	@Schema(description = "세션 담기 여부", example = "true")
	private Boolean isMySession;

	@Schema(description = "세션 라이브 여부", example = "true")
	private Boolean isLive;

	@Schema(description = "연사 정보")
	private SpeakerResponseDto speaker;

	@Schema(description = "태그 정보")
	private TagDto tags;

	public static SessionListResponseDto from(Session session, SpeakerResponseDto speaker, TagDto tags,
		Boolean isMySession) {
		return SessionListResponseDto.builder()
			.id(session.getSessionId())
			.title(session.getTitle())
			.summary(session.getSummary())
			.isMySession(isMySession)
			.isLive(session.getIsLive())
			.speaker(speaker)
			.tags(tags)
			.build();
	}
}
