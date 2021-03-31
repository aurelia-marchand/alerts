package com.safetynet.alerts.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.safetynet.alerts.dto.DistrictPersonDto;

import lombok.Data;

@Data
@Component
public class DistrictPeople {
	
	private List<DistrictPersonDto> districtPersonsDto;

	private int numberOfAdults;
	private int numberOfChildren;
	public DistrictPeople() {
		
	}
	
}
