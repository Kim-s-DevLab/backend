package eightplusone.bit.fit.domain.mysession.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "MySessionSessionIdRequestDto: 나의 세션 처리를 위한 SessionId 요청 Dto")
public class MySessionSessionIdRequestDto {
	@Min(value = 1, message = "세션 식별자는 1 이상이어야 합니다.")
	@NotNull(message = "세션 식별자가 입력되지 않았습니다.")
	@Schema(description = "세션 식별자", example = "1")
	private Long sessionId;

	public MySessionSessionIdRequestDto(Long sessionId) {
		this.sessionId = sessionId;
	}
}
