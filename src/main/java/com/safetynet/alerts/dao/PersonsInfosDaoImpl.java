package com.safetynet.alerts.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.alerts.dto.DistrictPersonDto;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonsInfosDaoImpl implements PersonsInfosDaoI {

	@Autowired
	Datas personsInfos;
	@Autowired
	AccessJsonI accessJson;
	@Autowired
	DistrictPersonDto personsByStationDto;

	@Override
	public List<Person> findPersonsByStationNumber(int station) {
		// Récupération des données du fichier Json via interface
		personsInfos = accessJson.getData();
		List<Person> persons = personsInfos.getPersons();
		List<Firestation> firestations = personsInfos.getFirestations();
		List<Person> personsArenvoyer = new ArrayList<>();
		// Boucle pour récupérer l'adress de la station puis comparer avec adresse des
		// personnes qu'on récupère si identique
		for (Firestation firestation : firestations) {
			log.debug("firestation n " + firestation);
			if (firestation.getStation() == station) {
				String address = firestation.getAddress();
				for (Person person : persons) {
					log.debug("person n " + person);
					if (person.getAddress().equalsIgnoreCase(address)) {
						personsArenvoyer.add(person);
					}
				}
			}
		}
		return personsArenvoyer;
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
	public List<MedicalRecord> findMedicalRecordsByListPerson(List<Person> personneList) {
		personsInfos = accessJson.getData();
		List<MedicalRecord> medicalRecords = personsInfos.getMedicalrecords();
		List<MedicalRecord> resultatMedicalRecordByName = new ArrayList<>();
		for (MedicalRecord medicalRecord : medicalRecords) {
			for (Person person : personneList) {
				if (medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())
						&& medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())) {
					resultatMedicalRecordByName.add(medicalRecord);
				}
			}
		}
		return resultatMedicalRecordByName;
	}

	@Override
	public int findStationByAddress(String address) {
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
	public Person findPersonByFistNameAndLastName(String firstName, String lastName) {
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
		for (Person person : persons) {
			if (person.getCity().equalsIgnoreCase(city)) {
				emails.add(person.getEmail());
			}
		}
		return emails;
	}

	@Override
	public List<String> findPhoneByStationNumber(int station) {
		List<Person> persons = accessJson.getData().getPersons();
		List<Firestation> firestations = accessJson.getData().getFirestations();
		List<String> phones = new ArrayList<>();
		for (Firestation firestation : firestations) {
			if (firestation.getStation() == station) {
				String address = firestation.getAddress();
				for (Person person : persons) {
					if (person.getAddress().equalsIgnoreCase(address)) {
						String phone = person.getPhone();
						phones.add(phone);
					}
				}
			}
		}

		return phones;
	}
}
