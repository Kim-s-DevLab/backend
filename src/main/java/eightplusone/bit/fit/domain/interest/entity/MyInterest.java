package eightplusone.bit.fit.domain.interest.entity;

import static jakarta.persistence.FetchType.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eightplusone.bit.fit.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "my_interest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyInterest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "my_interest_id")
	private Long id;

	@JsonIgnore
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "interest_id")
	private Interest interest;

	@JsonIgnore
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	private MyInterest(Interest interest, User user) {
		setInterest(interest);
		setUser(user);
	}

	public static MyInterest of(Interest interest, User user) {
		return MyInterest.builder()
			.interest(interest)
			.user(user)
			.build();
	}

	private void setInterest(Interest interest) {
		this.interest = interest;
		interest.getMyInterests().add(this);
	}

	private void setUser(User user) {
		this.user = user;
		user.getMyInterests().add(this);
	}

	public void updateInterest(Interest newInterests, User user) {
		setInterest(newInterests);
		setUser(user);
	}
}
