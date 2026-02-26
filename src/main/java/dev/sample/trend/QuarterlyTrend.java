package dev.sample.trend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class QuarterlyTrend {

	private final String quarter;

	private final long food;
	private final long car;
	private final long travelCulture;
	private final long insuranceHealth;
	private final long educationOffice;
	private final long shopping;
	private final long living;
	private final long home;
}