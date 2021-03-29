package com.safetynet.alerts.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.alerts.dto.DistrictPersonsDto;
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
	DistrictPersonsDto personsByStationDto;

	@Override
	public PersonsInfos findPersonsByStationNumber(int station) {
		// Récupération des données du fichier Json via interface
		personsInfos = accessJson.getData();

		List<Person> persons = personsInfos.getPersons();
		List<Firestation> firestations = personsInfos.getFirestations();

		PersonsInfos personsInfos = new PersonsInfos();
		personsInfos.setFirestations(firestations);
		personsInfos.setPersons(persons);

		
		return personsInfos;
	}

	@Override
	public List<MedicalRecord> findMedicalRecordsByPersons(List<DistrictPersonsDto> personsByStationDto2) {
		personsInfos = accessJson.getData();
		List<MedicalRecord> medicalRecords = personsInfos.getMedicalrecords();

		return medicalRecords;

	}

	@Override
	public List<Person> findPersonsByAddress(String address) {
		personsInfos = accessJson.getData();
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
		personsInfos = accessJson.getData();
		List<MedicalRecord> medicalRecords = personsInfos.getMedicalrecords();

		List<MedicalRecord> resultatMedicalRecordByName = new ArrayList<>();
		for (MedicalRecord medicalRecord : medicalRecords) {
			for (Person person : personneByAddress) {
				if (medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())
						&& medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())) {
					resultatMedicalRecordByName.add(medicalRecord);
				}
			}

		}

		return resultatMedicalRecordByName;
	}

	@Override
	public int getStationByAddress(String address) {
		personsInfos = accessJson.getData();
		List<Firestation> firestations = personsInfos.getFirestations();
		int station = 0;
		for (Firestation firestation : firestations) {
			if (firestation.getAddress().equalsIgnoreCase(address)) {
				station = firestation.getStation();
			}
		}
		return station;
	}

	@Override
	public Set<String> findAddressByStation(int station) {
		personsInfos = accessJson.getData();
		List<Firestation> firestations = personsInfos.getFirestations();
		Set<String> address = new HashSet<>();
		for (Firestation firestation : firestations) {
			if (firestation.getStation() == (station)) {

				address.add(firestation.getAddress());
			}
		}
		return address;
	}

	@Override
	public List<Person> findPersonsByStation(List<Integer> stations) {
		List<Integer> stationsList = stations;
		personsInfos = accessJson.getData();
		List<Firestation> firestations = personsInfos.getFirestations();
		List<Person> persons = personsInfos.getPersons();
		List<Person> personnes = new ArrayList<>();
		for (int station : stationsList) {

			for (Firestation firestation : firestations) {

				if (firestation.getStation() == station) {
					String address = firestation.getAddress();
					;
					for (Person person : persons) {

						if (person.getAddress().equalsIgnoreCase(address)) {

							personnes.add(person);

						}
					}
				}
			}
		}

		return personnes;
	}

	@Override
	public Person getPerson(String firstName, String lastName) {
		List<Person> persons = accessJson.getData().getPersons();
		for (Person person : persons) {
			if (person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)) {
				log.debug("dao renvoi : " + person);
				return person;
			}
		}
		return null;
	}

	@Override
	public MedicalRecord findMedicalRecordsByPerson(Person personne) {
		Person person = personne;
		MedicalRecord medicalRecordPerson = new MedicalRecord();
		List<MedicalRecord> medicalRecords = accessJson.getData().getMedicalrecords();
		
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())
					&& medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())) {
				
				medicalRecordPerson = medicalRecord;
				return medicalRecordPerson;
			}
		}
		return null;
	}

	@Override
	public List<String> findEmailByCity(String city) {
		List<Person> persons = accessJson.getData().getPersons();
		
		List<String> emails = new ArrayList<>();
		
		for(Person person : persons) {
			if(person.getCity().equalsIgnoreCase(city)) {
				emails.add(person.getEmail());
			}
		}
		
		
		return emails;
	}
}
