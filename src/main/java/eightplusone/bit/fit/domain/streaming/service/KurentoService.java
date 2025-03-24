package eightplusone.bit.fit.domain.streaming.service;

import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KurentoService {

	private final KurentoClient kurentoClient;

	public KurentoService(@Value("${spring.kurento.ws-url}") String kurentoUrl) {
		// KurentoClient를 지정한 URL로 초기화
		this.kurentoClient = KurentoClient.create(kurentoUrl);
	}

	public MediaPipeline createPipeline() {
		return kurentoClient.createMediaPipeline();
	}

	public WebRtcEndpoint createWebRtcEndpoint(MediaPipeline pipeline) {
		return new WebRtcEndpoint.Builder(pipeline).build();
	}
}
