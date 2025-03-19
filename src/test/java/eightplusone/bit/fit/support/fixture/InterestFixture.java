package eightplusone.bit.fit.support.fixture;

import eightplusone.bit.fit.domain.interest.entity.Interest;
import lombok.Getter;

@Getter
public enum InterestFixture {
	INTEREST_FIXTURE_1("데이터베이스"),
	INTEREST_FIXTURE_2("클라우드"),
	INTEREST_FIXTURE_3("백엔드");

	private final String name;

	InterestFixture(String name) {
		this.name = name;
	}

	public Interest createInterest() {
		return Interest.builder()
			.name(name)
			.build();
	}
}
