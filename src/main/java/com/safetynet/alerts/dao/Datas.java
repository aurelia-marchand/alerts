package com.safetynet.alerts.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import lombok.Data;

@Data
@Component
public class Datas {

	
	private List<Person> persons;
	
	private List<Firestation> firestations;
	
	private List<MedicalRecord> medicalrecords;
	
}
