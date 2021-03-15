package com.safetynet.alerts.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class PersonsByStationDto {

	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	
	
}
