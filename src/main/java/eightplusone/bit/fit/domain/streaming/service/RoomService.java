package eightplusone.bit.fit.domain.streaming.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import eightplusone.bit.fit.domain.session.service.SessionService;
import eightplusone.bit.fit.domain.streaming.entity.AudioRoom;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoomService {

	private final KurentoService kurentoService;

	private final Map<String, AudioRoom> rooms = new ConcurrentHashMap<>();
	private final SimpMessagingTemplate messagingTemplate;

	private final SessionService sessionService;

	public RoomService(KurentoService kurentoService, SimpMessagingTemplate messagingTemplate,
		SessionService sessionService) {
		this.kurentoService = kurentoService;
		this.messagingTemplate = messagingTemplate;
		this.sessionService = sessionService;
	}
}
