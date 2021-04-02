package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.alerts.dao.AccessJsonI;
import com.safetynet.alerts.dao.Datas;
import com.safetynet.alerts.dao.PersonsInfosDaoImpl;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

@WebMvcTest(PersonsInfosDaoImpl.class)
class PersonInfosDaoTest {

	@Autowired
	PersonsInfosDaoImpl personsInfosDaoImpl;

	@MockBean
	AccessJsonI accessJsonI;

	List<Person> persons = new ArrayList<>();
	List<Firestation> firestations = new ArrayList<>();
	List<MedicalRecord> medicalRecords = new ArrayList<>();

	Datas personsInfos = new Datas();

	MedicalRecord med1 = new MedicalRecord();
	MedicalRecord med2 = new MedicalRecord();
	MedicalRecord med3 = new MedicalRecord();
	MedicalRecord med4 = new MedicalRecord();
	MedicalRecord med5 = new MedicalRecord();
	Person personTest1 = new Person();
	Person personTest2 = new Person();
	Person personTest3 = new Person();
	Person personTest4 = new Person();
	Person personTest5 = new Person();

	@BeforeEach
	private void setUpPerTest() {
		personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544", "jaboyd@email.com");
		personTest2 = new Person("Jonhathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513", "drk@email.comm");
		personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		personTest4 = new Person("Tenley", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6512", "tenz@email.com");
		personTest5 = new Person("Tony", "Cooper", "112 Steppes Pl", "Culver", 97451, "841-874-6874", "tcoop@ymail.com");
		persons.add(personTest1);
		persons.add(personTest2);
		persons.add(personTest3);
		persons.add(personTest4);
		persons.add(personTest5);
		personsInfos.setPersons(persons);

		med1.setFirstName("Felicia");
		med1.setLastName("Boyd");
		med1.setBirthdate("01/08/1986");
		med1.setMedications(new ArrayList<>(Arrays.asList("tetracyclaz:650mg")));
		med1.setAllergies(new ArrayList<>(Arrays.asList("xilliathal")));

		med2.setFirstName("Jonhathan");
		med2.setLastName("Marrack");
		med2.setBirthdate("01/03/1989");

		med3.setFirstName("Tony");
		med3.setLastName("Cooper");
		med3.setBirthdate("03/06/1994");
		med3.setMedications(new ArrayList<>(Arrays.asList("hydrapermazol:300mg", "dodoxadin:30mg")));
		med3.setAllergies(new ArrayList<>(Arrays.asList("shellfish")));

		med4.setFirstName("Tessa");
		med4.setLastName("Carman");
		med4.setBirthdate("02/18/2012");

		med5.setFirstName("Tenley");
		med5.setLastName("Boyd");
		med5.setBirthdate("02/18/2012");
		med5.setAllergies(new ArrayList<>(Arrays.asList("peanut")));

		medicalRecords.add(med1);
		medicalRecords.add(med2);
		medicalRecords.add(med3);
		medicalRecords.add(med4);
		medicalRecords.add(med5);
		personsInfos.setMedicalrecords(medicalRecords);

		Firestation f1 = new Firestation();
		f1.setAddress("1509 Culver St");
		f1.setStation(3);

		Firestation f2 = new Firestation();
		f2.setAddress("29 15th St");
		f2.setStation(2);

		Firestation f3 = new Firestation();
		f3.setAddress("834 Binoc Ave");
		f3.setStation(3);

		Firestation f4 = new Firestation();
		f4.setAddress("112 Steppes Pl");
		f4.setStation(3);

		firestations.add(f1);
		firestations.add(f2);
		firestations.add(f3);
		firestations.add(f4);
		personsInfos.setFirestations(firestations);

		when(accessJsonI.getData()).thenReturn(personsInfos);
	}

	@Test
	void testfindPersonsByStationNumber() {
		// ARRANGE

		// ACT
		List<Person> personsResult = personsInfosDaoImpl.findPersonsByStationNumber(3);
		// ASSERT
		assertThat(personsResult.size()).isEqualTo(4);
	}

	@Test
	void testFindPersonsByAddress() {
		// ACT
		List<Person> result = personsInfosDaoImpl.findPersonsByAddress("1509 Culver St");
		// ASSERT
		assertThat(result.size()).isEqualTo(2);

	}

	@Test
	void testFindMedicalRecordsByListPerson() {
		// ARRANGE
		List<Person> personList = new ArrayList<>();
		personList.add(personTest1);
		personList.add(personTest2);
		personList.add(personTest3);
		// ACT
		List<MedicalRecord> result = personsInfosDaoImpl.findMedicalRecordsByListPerson(personList);
		// ASSERT
		assertThat(result.size()).isEqualTo(3);
	}
	
	@Test
	void testFindStationByAddress() {
		//ACT
		int result = personsInfosDaoImpl.findStationByAddress("112 Steppes Pl");
		//ASSERT
		assertThat(result).isEqualTo(3);
	}
	
	@Test
	void testFindAddressByStation() {
		//ACT
		Set<String> result = personsInfosDaoImpl.findAddressByStation(3);
		//ASSERT
		assertThat(result.size()).isEqualTo(3);
		assertThat(result).contains("112 Steppes Pl");
		assertThat(result).contains("834 Binoc Ave");
		assertThat(result).contains("1509 Culver St");

	}
	
	@Test
	void testFindPersonsByStation() {
		//ARRANGE
		List<Integer> stationsList = new ArrayList<>();
		stationsList.add(2);
		stationsList.add(3);
		
		//ACT
		List<Person> result = personsInfosDaoImpl.findPersonsByStation(stationsList);
		//ASSERT
		assertThat(result.size()).isEqualTo(5);
		
	}
	
	@Test
	void testFindPersonsByFirstNameAndLastName() {
		
		//ACT
		List<Person> result = personsInfosDaoImpl.findPersonByFistNameAndLastName("Tessa","Carman");
		//ASSERT
		assertThat(result).contains(personTest3);
	}
	
	@Test
	void testFindMedicalRecordByPerson() {
		
		//ACT
		MedicalRecord result = personsInfosDaoImpl.findMedicalRecordsByPerson(personTest3);
		//ASSERT
		assertThat(result).isEqualTo(med4);
	}
	
	@Test
	void testFindEmailByCity() {
		
		//ACT
		List<String> result = personsInfosDaoImpl.findEmailByCity("Culver");
		//ASSERT
		assertThat(result.size()).isEqualTo(5);
	}
	
	@Test
	void testFindPhoneByStationNumber() {
		
		//ACT
		List<String> result = personsInfosDaoImpl.findPhoneByStationNumber(3);
		//ASSERT
		assertThat(result.size()).isEqualTo(4);
	}
	
	

}
