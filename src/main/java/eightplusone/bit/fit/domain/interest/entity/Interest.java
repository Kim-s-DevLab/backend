package eightplusone.bit.fit.domain.interest.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@OneToMany(mappedBy = "interest", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MyInterest> myInterests = new ArrayList<>();

	@Builder
	private Interest(String name) {
		this.name = name;
	}

	public static Interest of(String name) {
		return Interest.builder()
			.name(name)
			.build();
	}
}
