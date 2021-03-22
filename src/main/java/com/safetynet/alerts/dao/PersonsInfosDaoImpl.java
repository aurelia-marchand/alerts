package com.safetynet.alerts.dao;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.alerts.dto.PersonsByStationDto;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonsInfosDaoImpl implements PersonsInfosDaoI {

	@Autowired
	PersonsInfos personsInfos;
	@Autowired
	AccessJsonI accessJson;
	@Autowired
	PersonsByStationDto personsByStationDto;

	@Override
	public  PersonsInfos findPersonsByStationNumber(int station) {
		// Récupération des données du fichier Json via interface
		personsInfos=accessJson.getData();
		
		List<Person> persons = personsInfos.getPersons();
		List<Firestation> firestations = personsInfos.getFirestations();
		
		PersonsInfos personsInfos = new PersonsInfos();
		personsInfos.setFirestations(firestations);
		personsInfos.setPersons(persons);

	
		return personsInfos;
	}

	@Override
	public List<MedicalRecord> findMedicalRecordsByPersons(List<PersonsByStationDto> personsByStationDto2) {
		personsInfos=accessJson.getData();
		List<MedicalRecord> medicalRecords = personsInfos.getMedicalrecords();

		return medicalRecords;

	}
}
