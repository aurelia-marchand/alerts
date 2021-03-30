package com.safetynet.alerts.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class DistrictDto {
	
	private List<DistrictPeopleDto> districtPersonsDto;

	private int numberOfAdults;
	private int numberOfChildren;
	public DistrictDto() {
		
	}
	
}
