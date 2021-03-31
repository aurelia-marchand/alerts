package com.safetynet.alerts.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.safetynet.alerts.dto.StreetPersonDto;

import lombok.Data;

@Data
@Component
public class StreetPeople {

	
	int station;
	List<StreetPersonDto> streetPeopleDto;
	
}
