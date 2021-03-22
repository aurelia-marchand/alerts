package com.safetynet.alerts.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Firestation {

	private String address;
	
	private int station;
	
}
