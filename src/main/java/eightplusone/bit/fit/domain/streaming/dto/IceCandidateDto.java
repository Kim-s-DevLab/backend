package eightplusone.bit.fit.domain.streaming.dto;

import org.kurento.client.IceCandidate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "IceCandidateDto: ICE candidate DTO")
public class IceCandidateDto {
	private String candidate;
	private String sdpMid;
	private int sdpMLineIndex;

	public static IceCandidateDto from(IceCandidate iceCandidate) {
		return IceCandidateDto.builder()
			.candidate(iceCandidate.getCandidate())
			.sdpMid(iceCandidate.getSdpMid())
			.sdpMLineIndex(iceCandidate.getSdpMLineIndex())
			.build();
	}
}