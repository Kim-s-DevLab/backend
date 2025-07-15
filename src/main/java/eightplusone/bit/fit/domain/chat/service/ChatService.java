package eightplusone.bit.fit.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eightplusone.bit.fit.domain.chat.dto.ChatMessageDto;
import eightplusone.bit.fit.domain.chat.entity.ChatMessage;
import eightplusone.bit.fit.domain.chat.enums.ChatCategory;
import eightplusone.bit.fit.domain.chat.repository.ChatLikeRepository;
import eightplusone.bit.fit.domain.chat.repository.ChatRepository;
import eightplusone.bit.fit.domain.user.repository.UserRedisRepository;
import eightplusone.bit.fit.domain.user.repository.UserRepository;
import eightplusone.bit.fit.global.exception.CustomException;
import eightplusone.bit.fit.global.exception.ErrorCode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatService {
	private final ChatRepository chatRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final UserRedisRepository userRedisRepository;
	private final ChatLikeRepository chatLikeRepository;
	private final UserRepository userRepository;
	private final ObjectMapper objectMapper;

	public ChatService(ChatRepository chatRepository, RedisTemplate<String, Object> redisTemplate,
		UserRedisRepository userRedisRepository, UserRepository userRepository, ChatLikeRepository chatLikeRepository,
		ObjectMapper objectMapper) {
		this.chatRepository = chatRepository;
		this.redisTemplate = redisTemplate;
		this.userRedisRepository = userRedisRepository;
		this.chatLikeRepository = chatLikeRepository;
		this.userRepository = userRepository;
		this.objectMapper = objectMapper;
	}

	public void createChatSession(Long sessionId) {
		if (chatRepository.existsBySessionId(String.valueOf(sessionId))) {
			log.info("이미 존재하는 채팅방: {}", sessionId);
			return;
		}

		chatRepository.createChatSession(String.valueOf(sessionId));
		log.info("새 채팅방 생성됨: {}", sessionId);
	}

	public void sendMessageWithEmail(ChatMessageDto dto, String email, Long sessionId) throws JsonProcessingException {
		if (dto.getMessage() == null || dto.getMessage().trim().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_MESSAGE_FORMAT);
		}

		if (dto.getMessage().length() > 300) {
			throw new CustomException(ErrorCode.MESSAGE_TOO_LONG);
		}

		if (!chatRepository.existsBySessionId(String.valueOf(sessionId))) {
			throw new CustomException(ErrorCode.CHAT_SESSION_NOT_FOUND);
		}

		var user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		String userId = user.getId().toString();
		String name = user.getName();

		userRedisRepository.saveUserName(userId, name);

		// 메시지 생성
		ChatMessage message = ChatMessage.builder()
			.sessionId(sessionId)
			.userId(userId)
			.category(dto.getCategory())
			.message(dto.getMessage())
			.build();

		chatRepository.saveMessage(message);

		// 질문이면 ZSet에도 추가
		if (message.getCategory() == ChatCategory.QUESTION) {
			String zsetKey = "questions:session:" + sessionId;
			redisTemplate.opsForZSet().add(zsetKey, message.getMessageId(), 0);
		}

		// ChatMessageDto로 다시 만들어서 발행 (messageId, timestamp 포함!)
		ChatMessageDto dtoToSend = ChatMessageDto.builder()
			.messageId(message.getMessageId())
			.category(message.getCategory())
			.message(message.getMessage())
			.name(name)
			.userId(message.getUserId())
			.sessionId(message.getSessionId())
			.timestamp(message.getTimestamp())
			.likes(0)
			.build();

		String redisKey = "chat-pub:" + sessionId;
		log.info("Redis 발행 메세지 : {} -> {}", redisKey, dtoToSend);
		redisTemplate.convertAndSend(redisKey, dtoToSend);  // messageId 있는 dto 전송
	}

	// 특정 채팅방의 최근 메시지 조회
	public List<ChatMessageDto> getRecentMessages(String sessionId) {
		if (!chatRepository.existsBySessionId(sessionId)) {
			throw new CustomException(ErrorCode.CHAT_SESSION_NOT_FOUND);
		}

		List<Object> rawMessages = chatRepository.getRecentMessages(sessionId);
		return rawMessages.stream()
			.map(obj -> {
				if (obj instanceof ChatMessage message) {
					String userName = userRedisRepository.getUserName(message.getUserId());
					Double score = redisTemplate.opsForZSet()
						.score("questions:session:" + sessionId, message.getMessageId());
					int likeCount = score != null ? score.intValue() : 0;

					return ChatMessageDto.builder()
						.messageId(message.getMessageId())
						.category(message.getCategory())
						.message(message.getMessage())
						.name(userName != null ? userName : "알 수 없음")
						.userId(message.getUserId())
						.sessionId(message.getSessionId())
						.timestamp(message.getTimestamp())
						.likes(likeCount)
						.build();
				}
				return null;
			})
			.filter(Objects::nonNull)
			.toList();
	}

	// 특정 채팅방 데이터 삭제 (강연 종료 후)
	public void clearChat(String sessionId) {
		if (!chatRepository.existsBySessionId(sessionId)) {
			throw new CustomException(ErrorCode.CHAT_SESSION_NOT_FOUND);
		}
		chatRepository.clearChat(sessionId);
	}

	// 채팅에서 userId를 기반으로 최신 사용자 이름을 가져올 수 있도록 Redis에 name 저장
	public void saveUserName(String userId, String name) {
		userRedisRepository.saveUserName(userId, name);
	}

	// 좋아요 추가
	public void likeMessage(String userId, Long sessionId, String messageId) {
		String likeKey = "like:" + sessionId + ":" + messageId;

		if (chatLikeRepository.hasLiked(likeKey, userId)) {
			throw new CustomException(ErrorCode.DUPLICATE_LIKE);
		}
		chatLikeRepository.likeMessage(likeKey, userId);

		// ZSet score 증가
		String zsetKey = "questions:session:" + sessionId;
		redisTemplate.opsForZSet().incrementScore(zsetKey, messageId, 1);

		// 좋아요 개수 조회
		int updatedLikeCount = getLikeCount(sessionId, messageId);

		// 좋아요 개수를 Redis Pub/Sub을 통해 전송
		String redisMessage = "{\"messageId\": \"" + messageId + "\", \"likes\": " + updatedLikeCount + "}";
		redisTemplate.convertAndSend("chat-likes", redisMessage);

		log.info("좋아요 변경사항 Redis Pub/Sub 전송: {}", redisMessage);

		// TOP3 계산 및 chat-top3 채널 발행
		List<ChatMessageDto> top3 = getZSetSortedQuestions(sessionId, 0, 3);
		try {
			String top3Json = objectMapper.writeValueAsString(top3);
			String top3Channel = "chat-top3:" + sessionId;
			redisTemplate.convertAndSend(top3Channel, top3Json);
			log.info("TOP3 메시지 Redis 발행 완료: {}", top3Json);
		} catch (JsonProcessingException e) {
			throw new CustomException(ErrorCode.JSON_SERIALIZATION_FAILED);
		}
	}

	// 좋아요 취소
	public void unlikeMessage(String userId, Long sessionId, String messageId) {
		String likeKey = "like:" + sessionId + ":" + messageId;

		if (!chatLikeRepository.hasLiked(likeKey, userId)) {
			throw new CustomException(ErrorCode.CANNOT_UNLIKE);
		}
		chatLikeRepository.unlikeMessage(likeKey, userId, sessionId, messageId);

		// ZSet score 감소
		String zsetKey = "questions:session:" + sessionId;
		redisTemplate.opsForZSet().incrementScore(zsetKey, messageId, -1);

		// 좋아요 개수 조회
		int updatedLikeCount = getLikeCount(sessionId, messageId);

		// 좋아요 개수를 Redis Pub/Sub을 통해 전송
		String redisMessage = "{\"messageId\": \"" + messageId + "\", \"likes\": " + updatedLikeCount + "}";
		redisTemplate.convertAndSend("chat-likes", redisMessage);

		log.info("좋아요 변경사항 Redis Pub/Sub 전송: {}", redisMessage);

		// TOP3 계산 및 chat-top3 채널 발행
		List<ChatMessageDto> top3 = getZSetSortedQuestions(sessionId, 0, 3);
		try {
			String top3Json = objectMapper.writeValueAsString(top3);
			String top3Channel = "chat-top3:" + sessionId;
			redisTemplate.convertAndSend(top3Channel, top3Json);
			log.info("TOP3 메시지 Redis 발행 완료: {}", top3Json);
		} catch (JsonProcessingException e) {
			throw new CustomException(ErrorCode.JSON_SERIALIZATION_FAILED);
		}
	}

	public void likeMessageWithEmail(String email, Long sessionId, String messageId) {
		var user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		String userId = user.getId().toString();
		likeMessage(userId, sessionId, messageId);
	}

	public void unlikeMessageWithEmail(String email, Long sessionId, String messageId) {
		var user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		String userId = user.getId().toString();
		unlikeMessage(userId, sessionId, messageId);
	}

	// 좋아요 개수 조회
	public int getLikeCount(Long sessionId, String messageId) {
		String likeKey = "like:" + sessionId + ":" + messageId;
		return chatLikeRepository.getLikeCount(likeKey);
	}

	// 사용자가 해당 메시지에 좋아요를 눌렀는지 확인
	public boolean hasLiked(String userId, Long sessionId, String messageId) {
		String likeKey = "like:" + sessionId + ":" + messageId;
		return chatLikeRepository.hasLiked(likeKey, userId);
	}

	public List<ChatMessageDto> getZSetSortedQuestions(Long sessionId, int page, int size) {
		String zsetKey = "questions:session:" + sessionId;
		int start = page * size;
		int end = start + size - 1;

		Set<Object> messageIds = redisTemplate.opsForZSet().reverseRange(zsetKey, start, end);
		if (messageIds == null || messageIds.isEmpty())
			return Collections.emptyList();

		return messageIds.stream()
			.map(messageIdObj -> {
				String messageId = messageIdObj.toString().replace("\"", "");
				String raw = (String)redisTemplate.opsForValue().get("chat-messages:" + messageId);
				if (raw == null) {
					log.warn("⚠️ 메시지를 찾을 수 없습니다. messageId: {}", messageId);
					return null;
				}

				try {
					ChatMessage msg = objectMapper.readValue(raw, ChatMessage.class);
					String userName = userRedisRepository.getUserName(msg.getUserId());
					Double score = redisTemplate.opsForZSet().score(zsetKey, messageId);
					int likeCount = score != null ? score.intValue() : 0;

					return ChatMessageDto.builder()
						.messageId(msg.getMessageId())
						.category(msg.getCategory())
						.message(msg.getMessage())
						.name(userName != null ? userName : "알 수 없음")
						.userId(msg.getUserId())
						.sessionId(msg.getSessionId())
						.timestamp(msg.getTimestamp())
						.likes(likeCount)
						.build();

				} catch (JsonProcessingException e) {
					log.error("❌ 메시지 역직렬화 실패: {}", e.getMessage());
					return null;
				}
			})
			.filter(Objects::nonNull)
			.toList();
	}

}
