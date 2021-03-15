package com.safetynet.alerts.dao;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.alerts.dto.PersonsByStationDto;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PersonsInfosDaoImpl implements PersonsInfosDaoI {

	@Autowired
	PersonsInfos personsInfos;
	@Autowired
	AccessJsonI accessJson;
	@Autowired
	PersonsByStationDto personsByStationDto;

	@Override
	public List<PersonsByStationDto> findPersonsByStationNumber(int station) {
		// Récupération des données du fichier Json via interface
		personsInfos=accessJson.getData();
		log.debug("test");
		List<Person> persons = personsInfos.getPersons();
		List<Firestation> firestations = personsInfos.getFirestations();

		List<PersonsByStationDto> liste = new ArrayList<>();
		// Boucle pour récupérer l'adress de la station puis comparer avec adresse des personnes qu'on récupère si identique
		for (Firestation firestation : firestations) {
			if (firestation.getStation() == station) {
				String address = firestation.getAddress();
				for (Person person : persons) {
					if (person.getAddress().equalsIgnoreCase(address)) {
						// Utilisation ModelMapper pour map Dto/entité
						ModelMapper modelMapper = new ModelMapper();
						personsByStationDto = modelMapper.map(person, PersonsByStationDto.class);
						liste.add(personsByStationDto);
					}
				}
			}
		}
		return liste;
	}

	@Override
	public List<MedicalRecord> findMedicalRecordsByPersons(List<PersonsByStationDto> personsByStationDto2) {
		personsInfos=accessJson.getData();
		List<MedicalRecord> medicalRecords = personsInfos.getMedicalrecords();

		return medicalRecords;

	}
}
