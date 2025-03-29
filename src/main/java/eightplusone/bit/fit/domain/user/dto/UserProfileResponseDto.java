package eightplusone.bit.fit.domain.user.dto;

import java.util.List;

import eightplusone.bit.fit.domain.interest.entity.MyInterest;
import eightplusone.bit.fit.domain.user.entity.enums.YearLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "UserProfileResponseDto: 회원 개인 정보 Dto")
public class UserProfileResponseDto {

	@Schema(description = "직무", example = "백엔드")
	private final String job;
	@Schema(description = "연차", example = "신입")
	private final YearLevel years;
	@Schema(description = "사용자의 관심 분야", example = "[\"대출\", \"디지털 뱅킹\", \"마이데이터\"]")
	private final List<String> interests;

	@Builder
	private UserProfileResponseDto(String job, YearLevel years, List<String> interests) {
		this.job = job;
		this.years = years;
		this.interests = interests;
	}

	public static UserProfileResponseDto of(String job, YearLevel years, List<MyInterest> myInterests) {
		return UserProfileResponseDto.builder()
			.job(job)
			.years(years)
			.interests(myInterests.stream().map(myInterest -> myInterest.getInterest().getName()).toList())
			.build();
	}
}
