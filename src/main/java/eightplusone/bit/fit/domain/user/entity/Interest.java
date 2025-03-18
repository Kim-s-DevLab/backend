package eightplusone.bit.fit.domain.user.entity;

import static jakarta.persistence.FetchType.*;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "interest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "interest_id")
	private Long id;

	@Column(name = "name", nullable = false, length = 30)
	private String name;

	@JsonIgnore
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	private Interest(String name) {
		this.name = name;
	}

	public static Interest of(String name) {
		return Interest.builder()
			.name(name)
			.build();
	}

	public static List<Interest> from(List<String> names) {
		return names.stream()
			.map(Interest::of)
			.collect(Collectors.toList());
	}

	protected void setUser(User user) {
		this.user = user;
		user.getInterests().add(this);
	}
}
