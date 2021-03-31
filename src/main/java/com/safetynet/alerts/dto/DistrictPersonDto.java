package com.safetynet.alerts.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class DistrictPersonDto {

	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	
	
}
