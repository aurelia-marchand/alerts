package com.safetynet.alerts.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class PersonsInfos {

	
	private List<Person> persons;
	
	private List<Firestation> firestations;
	
	private List<MedicalRecord> medicalrecords;
	
}
