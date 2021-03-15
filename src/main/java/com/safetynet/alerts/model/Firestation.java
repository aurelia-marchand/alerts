package com.safetynet.alerts.model;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class Firestation {

	private String address;
	
	private int station;
	
}
