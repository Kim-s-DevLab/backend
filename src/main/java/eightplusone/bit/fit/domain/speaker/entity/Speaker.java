package eightplusone.bit.fit.domain.speaker.entity;

import eightplusone.bit.fit.domain.session.entity.Session;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "speakers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Speaker {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long speakerId;

	@Column(nullable = false, length = 200)
	private String image;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false, length = 200)
	private String description;

	@OneToOne
	@JoinColumn(name = "session_id", nullable = false)
	private Session session;
}
