package eightplusone.bit.fit.domain.mysession.dto;

import java.time.format.DateTimeFormatter;

import eightplusone.bit.fit.domain.session.entity.Session;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "MySessionLikedSessionsResponseDto: 좋아요 담기한 나의 세션 리스트 Dto")
public class MySessionLikedSessionsResponseDto {
	@Schema(description = "강연 식별자", example = "1")
	private final Long sessionId;
	@Schema(description = "강연 제목", example = "백엔드 마스터하기")
	private final String title;
	@Schema(description = "강연 요약", example = "자바 잘하는법")
	private final String summary;
	@Schema(description = "강연 시작 시간", example = "202504020950")
	private final String startTime;
	@Schema(description = "강연 종료 시간", example = "202504021050")
	private final String endTime;

	@Builder
	private MySessionLikedSessionsResponseDto(Long sessionId, String title, String summary, String startTime,
		String endTime) {
		this.sessionId = sessionId;
		this.title = title;
		this.summary = summary;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public static MySessionLikedSessionsResponseDto from(Session session) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
		return MySessionLikedSessionsResponseDto.builder()
			.sessionId(session.getSessionId())
			.title(session.getTitle())
			.summary(session.getSummary())
			.startTime(session.getStartTime().format(formatter))
			.endTime(session.getEndTime().format(formatter))
			.build();
	}
}
