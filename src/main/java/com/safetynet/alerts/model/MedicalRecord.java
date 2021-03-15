package com.safetynet.alerts.model;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class MedicalRecord {

	private String firstName;
	
	private String lastName;
	
	private String birthdate;
	
	private List<String> medications;
	
	private List<String> allergies;

}
