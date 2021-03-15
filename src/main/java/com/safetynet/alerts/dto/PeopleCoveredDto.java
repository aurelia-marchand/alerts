package com.safetynet.alerts.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class PeopleCoveredDto {
	
	private List<PersonsByStationDto> personsByStationDto;

	private int numberOfAdults;
	private int numberOfChildren;
	public PeopleCoveredDto() {
		
	}
	
}
