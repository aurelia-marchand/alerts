package com.safetynet.alerts.dao;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<Person> findPersonsByAddress(String address) {
		personsInfos=accessJson.getData();
		List<Person> persons = personsInfos.getPersons();
		
		List<Person> resultPersonByAddres = new ArrayList<>();
		for (Person person : persons) {
			if (person.getAddress().equalsIgnoreCase(address)) {
				resultPersonByAddres.add(person);
			}
		}
		
		return resultPersonByAddres;
	}

	@Override
	public List<MedicalRecord> findMedicalRecordsByPerson(List<Person> personneByAddress) {
		personsInfos=accessJson.getData();
		List<MedicalRecord> medicalRecords = personsInfos.getMedicalrecords();

		List<MedicalRecord> resultatMedicalRecordByName = new ArrayList<>();
		for (MedicalRecord medicalRecord : medicalRecords) {
			for(Person person : personneByAddress) {
				if (medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())&&medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())) {
					resultatMedicalRecordByName.add(medicalRecord);
				}
			}
			
		}
		
		return resultatMedicalRecordByName;
	}

	@Override
	public int getStationByAddress(String address) {
		personsInfos=accessJson.getData();
		List<Firestation> firestations = personsInfos.getFirestations();
		int station = 0;
		for(Firestation firestation : firestations) {
			if (firestation.getAddress().equalsIgnoreCase(address)) {
				station = firestation.getStation();			}
		}
		return station;
	}
}
