package com.safetynet.alerts.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class City {

	private int zip;
	
	private String cityName;
}
