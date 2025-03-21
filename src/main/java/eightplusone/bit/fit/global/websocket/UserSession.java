package eightplusone.bit.fit.global.websocket;

/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kurento.client.IceCandidate;
import org.kurento.client.WebRtcEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonObject;

import lombok.Getter;

/**
 * User session.
 *
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 5.0.0
 */
public class UserSession {

	private static final Logger log = LoggerFactory.getLogger(UserSession.class);

	@Getter
	private final WebSocketSession session;

	@Getter
	private WebRtcEndpoint webRtcEndpoint;

	// ICE Candidate를 임시 저장할 큐(또는 리스트)
	private final List<IceCandidate> candidateQueue = new ArrayList<>();

	private boolean endpointReady = false;

	public UserSession(WebSocketSession session) {
		this.session = session;
	}

	public void sendMessage(JsonObject message) throws IOException {
		log.debug("Sending message from user with session Id '{}': {}", session.getId(), message);
		session.sendMessage(new TextMessage(message.toString()));
	}

	public synchronized void addCandidate(IceCandidate candidate) {
		// 아직 endpoint가 준비되지 않았다면, candidateQueue에 쌓아둠
		if (!endpointReady || webRtcEndpoint == null) {
			candidateQueue.add(candidate);
		} else {
			log.info("Kurento: ICE Candidate added for session '{}': {}", session.getId(), candidate.getCandidate());
			webRtcEndpoint.addIceCandidate(candidate);
		}
	}

	// 아직 endpointReady=false 이므로, ICE는 버퍼에만 쌓인다.
	public synchronized void setWebRtcEndpoint(WebRtcEndpoint endpoint) {
		this.webRtcEndpoint = endpoint;
	}

	public synchronized void markEndpointReady() {
		this.endpointReady = true;

		// 버퍼에 쌓인 ICE Candidate가 있다면, 모두 반영
		for (IceCandidate candidate : candidateQueue) {
			webRtcEndpoint.addIceCandidate(candidate);
		}
		candidateQueue.clear();
	}

	public void close() throws IOException {
		if (webRtcEndpoint != null) {
			webRtcEndpoint.release();
		}
		if (session.isOpen()) {
			session.close();
		}
	}
}
