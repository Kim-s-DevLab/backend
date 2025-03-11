package eightplusone.bit.fit.domain.session.entity.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CongestionLevel {
	RELAXED("여유", 0, 50),
	MODERATE("적정", 50, 80),
	CROWDED("혼잡", 80, Integer.MAX_VALUE);

	private final String label;
	private final int min;
	private final int max;

	public static String fromPercent(double percent) {
		return Arrays.stream(values())
			.filter(level -> percent >= level.min && percent < level.max)
			.findFirst()
			.map(CongestionLevel::getLabel)
			.orElseThrow(() -> new IllegalArgumentException("오류 발생"));
	}
}
