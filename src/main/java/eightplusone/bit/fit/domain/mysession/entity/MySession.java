package eightplusone.bit.fit.domain.mysession.entity;

import static jakarta.persistence.FetchType.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eightplusone.bit.fit.domain.mysession.enums.MySessionType;
import eightplusone.bit.fit.domain.session.entity.Session;
import eightplusone.bit.fit.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "my_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MySession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "my_session_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", length = 20, nullable = false)
	private MySessionType type;

	@JsonIgnore
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonIgnore
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "session_id")
	private Session session;

	@Builder
	private MySession(MySessionType type, User user, Session session) {
		this.type = type;
		setSession(session);
		setUser(user);
	}

	public static MySession register(User user, Session session) {
		return MySession.builder()
			.type(MySessionType.REGISTER)
			.user(user)
			.session(session)
			.build();
	}

	private void setSession(Session session) {
		this.session = session;
		session.getMySessions().add(this);
	}

	private void setUser(User user) {
		this.user = user;
		user.getMySessions().add(this);
	}
}
