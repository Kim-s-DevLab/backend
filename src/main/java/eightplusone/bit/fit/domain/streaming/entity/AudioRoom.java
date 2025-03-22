package eightplusone.bit.fit.domain.streaming.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;

import lombok.Setter;

public class AudioRoom {
	private final String roomId;
	private final MediaPipeline pipeline;

	// 발표자 Endpoint (1명)
	@Setter
	private WebRtcEndpoint presenterEndpoint;

	// 청중 Endpoint (N명)
	private final Map<String, WebRtcEndpoint> audienceEndpoints = new ConcurrentHashMap<>();

	public AudioRoom(String roomId, MediaPipeline pipeline) {
		this.roomId = roomId;
		this.pipeline = pipeline;
	}

	public String getRoomId() {
		return roomId;
	}

	public MediaPipeline getPipeline() {
		return pipeline;
	}

	public WebRtcEndpoint getPresenterEndpoint() {
		return presenterEndpoint;
	}

	public Map<String, WebRtcEndpoint> getAudienceEndpoints() {
		return audienceEndpoints;
	}
}
