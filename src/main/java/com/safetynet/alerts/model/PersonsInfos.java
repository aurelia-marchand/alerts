package com.safetynet.alerts.model;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class PersonsInfos {

	
	private List<Person> persons;
	
	private List<Firestation> firestations;
	
	private List<MedicalRecord> medicalrecords;
	
}
