package com.safetynet.alerts.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class DistricDto {
	
	private List<DistrictPersonsDto> districtPersonsDto;

	private int numberOfAdults;
	private int numberOfChildren;
	public DistricDto() {
		
	}
	
}
