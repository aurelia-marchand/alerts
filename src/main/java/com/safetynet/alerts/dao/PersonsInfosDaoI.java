package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.dto.PersonsByStationDto;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;

public interface PersonsInfosDaoI {
	
	public PersonsInfos findPersonsByStationNumber(int station);

	public List<MedicalRecord> findMedicalRecordsByPersons(List<PersonsByStationDto> personsByStationDto2);

	public List<Person> findPersonsByAddress(String address);

	public List<MedicalRecord> findMedicalRecordsByPerson(List<Person> personneByAddress);
}
