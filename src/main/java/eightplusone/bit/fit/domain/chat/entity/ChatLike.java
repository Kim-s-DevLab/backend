package eightplusone.bit.fit.domain.chat.entity;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@RedisHash(value = "chat_like")
@Getter
public class ChatLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long likeId; // 좋아요 PK

	private Long userId; // 사용자 아이디

	@ManyToOne
	@JoinColumn(name = "message_id", nullable = false)
	private ChatMessage chatMessage; // 메시지 PK (외래키)

}

