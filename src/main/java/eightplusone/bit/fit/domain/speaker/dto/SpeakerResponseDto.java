package eightplusone.bit.fit.domain.speaker.dto;

import eightplusone.bit.fit.domain.speaker.entity.Speaker;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpeakerResponseDto {
	private String image;

	private String name;

	public static SpeakerResponseDto from(Speaker speaker) {
		return SpeakerResponseDto.builder()
			.image(speaker.getImage())
			.name(speaker.getName())
			.build();
	}
}
