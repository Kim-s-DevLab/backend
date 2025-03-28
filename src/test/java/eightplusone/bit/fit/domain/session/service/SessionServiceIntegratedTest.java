package eightplusone.bit.fit.domain.session.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import eightplusone.bit.fit.domain.mysession.repository.MySessionRepository;
import eightplusone.bit.fit.domain.session.repository.SessionRepository;
import eightplusone.bit.fit.domain.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SessionServiceIntegrationTest {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private MySessionRepository mySessionRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private final String SESSION_USER_KEY = "session_user";

	@BeforeEach
	void clearRedis() {
		redisTemplate.delete("session_user");
		redisTemplate.delete("session_congestion");
	}

	@Test
	@DisplayName("체크인과 체크아웃을 Redis와 실제로 수행한다")
	void checkInAndOut_IntegrationTest() {
		// Given
		String email = "integration@test.com";

		// When
		sessionService.checkIn(email);

		// Then
		Object value = redisTemplate.opsForHash().get(SESSION_USER_KEY, email);
		assertThat(value).isEqualTo("null");

		// When
		sessionService.checkOut(email);

		// Then
		Boolean exists = redisTemplate.opsForHash().hasKey(SESSION_USER_KEY, email);
		assertThat(exists).isFalse();
	}

	@Test
	@DisplayName("1000명 체크인을 동시에 수행한다.")
	void checkIn_concurrently() throws InterruptedException {
		// Given
		int numberOfUsers = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		CountDownLatch latch = new CountDownLatch(numberOfUsers);

		// When
		for (int i = 0; i < numberOfUsers; i++) {
			int finalI = i;
			executorService.execute(() -> {
				try {
					String email = "user" + finalI + "@test.com";
					sessionService.checkIn(email);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// Then
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(SESSION_USER_KEY);
		Collection<Object> values = entries.values();

		assertThat(entries).hasSize(numberOfUsers);
		assertThat(values).allMatch(value -> value.equals("null"));
	}

	@Test
	@DisplayName("1000명 체크아웃을 동시에 수행한다.")
	void checkOut_concurrently() throws InterruptedException {
		// Given
		int numberOfUsers = 1000;
		for (int i = 0; i < numberOfUsers; i++) {
			String email = "user" + i + "@test.com";
			sessionService.checkIn(email);
		}

		ExecutorService executorService = Executors.newFixedThreadPool(100);
		CountDownLatch latch = new CountDownLatch(numberOfUsers);

		// When
		for (int i = 0; i < numberOfUsers; i++) {
			int finalI = i;
			executorService.execute(() -> {
				try {
					String email = "user" + finalI + "@test.com";
					sessionService.checkOut(email);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// Then
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(SESSION_USER_KEY);
		assertThat(entries).isEmpty();
	}
}
