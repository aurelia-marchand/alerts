package com.safetynet.alerts.dao;

import java.util.List;
import java.util.Set;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

public interface PersonsInfosDaoI {
	
	public List<Person> findPersonsByStationNumber(int station);

	public List<Person> findPersonsByAddress(String address);

	public List<MedicalRecord> findMedicalRecordsByListPerson(List<Person> personneByAddress);

	public int findStationByAddress(String address);

	public Set<String> findAddressByStation(int station);

	public List<Person> findPersonsByStation(List<Integer> stations);

	public List<Person> findPersonByFistNameAndLastName(String firstName, String lastName);

	public MedicalRecord findMedicalRecordsByPerson(Person person);

	public List<String> findEmailByCity(String city);

	public List<String> findPhoneByStationNumber(int station);
}
