package eightplusone.bit.fit.global.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import eightplusone.bit.fit.domain.chat.dto.ChatMessageDto;
import eightplusone.bit.fit.domain.chat.enums.ChatCategory;

@Component
public class ChatSubscriber implements MessageListener {
	private final SimpMessagingTemplate messagingTemplate;

	public ChatSubscriber(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			System.out.println("🔹 Redis에서 메시지 수신: " + new String(message.getBody()));

			// "userId|category|message" 형식으로 되어있는 메시지를 분해
			String[] parts = new String(message.getBody()).split("\\|");
			if (parts.length == 3) {
				ChatMessageDto chatMessage = new ChatMessageDto();
				chatMessage.setUserId(Long.parseLong(parts[0]));  // userId
				chatMessage.setCategory(ChatCategory.valueOf(parts[1]));               // category
				chatMessage.setMessage(parts[2]);                // message 내용

				// WebSocket을 통해 구독자에게 메시지 전송
				messagingTemplate.convertAndSend("/sub/chat", chatMessage);
				System.out.println("✅ WebSocket으로 전송 완료: " + chatMessage);
			} else {
				System.out.println("❌ 메시지 포맷 오류: " + new String(message.getBody()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
