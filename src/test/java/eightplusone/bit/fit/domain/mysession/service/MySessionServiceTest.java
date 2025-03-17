package eightplusone.bit.fit.domain.mysession.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import eightplusone.bit.fit.domain.mysession.entity.MySession;
import eightplusone.bit.fit.domain.mysession.enums.MySessionType;
import eightplusone.bit.fit.domain.mysession.repository.MySessionRepository;
import eightplusone.bit.fit.domain.session.entity.Session;
import eightplusone.bit.fit.domain.session.repository.SessionRepository;
import eightplusone.bit.fit.domain.user.entity.User;
import eightplusone.bit.fit.domain.user.repository.UserRepository;
import eightplusone.bit.fit.global.exception.CustomException;
import eightplusone.bit.fit.global.exception.ErrorCode;
import eightplusone.bit.fit.support.fixture.SessionFixture;
import eightplusone.bit.fit.support.fixture.UserFixture;

@SpringBootTest
@Transactional
class MySessionServiceTest {

	@Autowired
	MySessionService mySessionService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	SessionRepository sessionRepository;
	@Autowired
	MySessionRepository mySessionRepository;

	@Test
	@DisplayName("사용자는 강의 미리 담기를 한다")
	void register() {
		//given
		User userFixture = UserFixture.USER_FIXTURE_1.createUser();
		User user = userRepository.save(userFixture);

		Session sessionFixture = SessionFixture.SESSION_STAGE_1_FIXTURE_1.createSession();
		Session session = sessionRepository.save(sessionFixture);
		//when
		mySessionService.registerMySession(user.getEmail(), session.getSessionId());

		//then
		MySession mySession = mySessionRepository.findSessionByUserId(user.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
		assertAll(
			() -> assertThat(mySession.getType()).isEqualTo(MySessionType.REGISTER),
			() -> assertThat(mySession.getSession().getSessionId()).isEqualTo(session.getSessionId()),
			() -> assertThat(mySession.getSession().getTitle()).isEqualTo(session.getTitle()),
			() -> assertThat(mySession.getSession().getSummary()).isEqualTo(session.getSummary()),
			() -> assertThat(mySession.getSession().getStartTime()).isEqualTo(session.getStartTime()),
			() -> assertThat(mySession.getSession().getEndTime()).isEqualTo(session.getEndTime()),
			() -> assertThat(mySession.getSession().getStandardCount()).isEqualTo(session.getStandardCount()),
			() -> assertThat(mySession.getSession().getAudioChannel()).isEqualTo(session.getAudioChannel())
		);
	}
}
