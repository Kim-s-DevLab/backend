package eightplusone.bit.fit.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequestDto {

	private String job;
	private Integer years;
	private String interests;

	@Builder
	private UserProfileUpdateRequestDto(String job, Integer years, String interests) {
		this.job = job;
		this.years = years;
		this.interests = interests;
	}

	public static UserProfileUpdateRequestDto of(String job, Integer years, String interests) {
		return UserProfileUpdateRequestDto.builder()
			.job(job)
			.years(years)
			.interests(interests)
			.build();
	}
}
