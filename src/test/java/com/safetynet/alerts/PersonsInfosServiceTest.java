package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.safetynet.alerts.dao.PersonsInfosDaoI;
import com.safetynet.alerts.dto.AlertPhoneDto;
import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.ChildrenByAddressDto;
import com.safetynet.alerts.dto.ChildrenFamilyDto;
import com.safetynet.alerts.dto.CommunityEmailDto;
import com.safetynet.alerts.dto.DistrictDto;
import com.safetynet.alerts.dto.DistrictPeopleDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.StationsDto;
import com.safetynet.alerts.dto.StreetDto;
import com.safetynet.alerts.dto.StreetPeopleDto;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonsInfosServiceImpl;

import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;

@WebMvcTest(PersonsInfosServiceImpl.class)
@RunWith(MockitoJUnitRunner.class)
@Slf4j
class PersonsInfosServiceTest {
	
	@MockBean
	PersonsInfosDaoI personsInfosDaoI;
	
	
	@Autowired
	PersonsInfosServiceImpl personsInfosServiceImpl;


	
	List<MedicalRecord> medicalRecordList = new ArrayList<>();
	List<Person> person = new ArrayList<>();
	Person personTest1 = new Person();
	Person personTest2 = new Person();
	Person personTest3 = new Person();
	Person personTest4 = new Person();
	Person personTest5 = new Person();
	
	MedicalRecord medTest1 = new MedicalRecord();
	MedicalRecord medTest2 = new MedicalRecord();
	MedicalRecord medTest3 = new MedicalRecord();
	MedicalRecord medTest4 = new MedicalRecord();
	MedicalRecord medTest5 = new MedicalRecord();

	
	
	
	@BeforeEach
	private void setUpPerTest() {
		List<String> medications = new ArrayList<>();
		List<String> allergies = new ArrayList<>();
		medications.add("dolipranne");
		allergies.add("noisette");
		
		medTest1 = new MedicalRecord("Felicia", "Boyd", "03/11/2010", medications, allergies);
		medTest2 = new MedicalRecord("Jonanathan", "Marrack", "03/11/2010", medications, allergies);
		medTest3 = new MedicalRecord("Tessa", "Carman", "03/11/2010", medications, allergies);
		medTest4 = new MedicalRecord("Aurelia", "Marchand", "03/11/2010", medications, allergies);

		medTest5 = new MedicalRecord("Jonanathan", "Marrack", "12/29/1976", medications, allergies);
		
		
		medicalRecordList.add(medTest1);		
		medicalRecordList.add(medTest2);
		medicalRecordList.add(medTest3);
		medicalRecordList.add(medTest4);
		medicalRecordList.add(medTest5);
		
		personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544", "jaboyd@email.com");
		personTest2 = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513", "drk@email.comm");
		personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		personTest4 = new Person("Aurelia", "Marchand", "29 15th St", "Culver", 97451, "841-874-6513", "drk@email.comm");
		personTest5 = new Person("Jonathan", "Marrack", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		
		
		person.add(personTest1);		
		person.add(personTest2);
		person.add(personTest3);
		person.add(personTest4);
		person.add(personTest5);

	}

	@Test
	void TestcalculateNumberOfChildren(){
		//ACT
		int result = PersonsInfosServiceImpl.calculateNumberOfChildren(medicalRecordList);
		//ASSERT
		assertThat(result).isEqualTo(4);

	}
	
	@Test
	void TestcalculateNumberOfAdult(){
		//ACT
		int result = PersonsInfosServiceImpl.calculateNumberOfAdults(medicalRecordList);
		//ASSERT
		assertThat(result).isEqualTo(1);

	}
	
	@Test
	void TestCalculateAge() {
		// ARRANGE
		String birthdate = "07/13/1984";
		
		// ACT
		int result = PersonsInfosServiceImpl.calculateAge(birthdate);
		
		
		// ASSERT
		assertThat(result).isEqualTo(36);
	}
	

	@Test
	void testGetListPersonsByStationNumber() {
		// ARRANGE
		DistrictDto peopleCoveredDto = new DistrictDto();
		
		when(personsInfosDaoI.findPersonsByStationNumber(1)).thenReturn(person);
		when(personsInfosDaoI.findMedicalRecordsByListPerson(person)).thenReturn(medicalRecordList);
		
		// ACT
		peopleCoveredDto = personsInfosServiceImpl.getListPersonsByStationNumber(1);
		
		// ASSERT
		assertThat(peopleCoveredDto.getNumberOfAdults()).isEqualTo(1);
		
		verify(personsInfosDaoI).findPersonsByStationNumber(1);
		verify(personsInfosDaoI).findMedicalRecordsByListPerson(person);
	}
	
	@Test
	void testGetListChildrenByAddress() {
		// ARRANGE
		ChildAlertDto childAlert = new ChildAlertDto();

		when(personsInfosDaoI.findPersonsByAddress("1509 Culver St")).thenReturn(person);
		when(personsInfosDaoI.findMedicalRecordsByListPerson(person)).thenReturn(medicalRecordList);

		// ACT
		childAlert = personsInfosServiceImpl.getListChildrenByAddress("1509 Culver St");
		
		// ASSERT
		assertThat(childAlert.getChildrenByAdress().size()).isEqualTo(4);
		
		verify(personsInfosDaoI).findPersonsByAddress("1509 Culver St");
		verify(personsInfosDaoI).findMedicalRecordsByListPerson(person);
	}
	
	@Test
	void testGetListPhoneByStation() {
		// ARRANGE
		AlertPhoneDto phoneAlertDto = new AlertPhoneDto();
		List<String> phones = new ArrayList<>();
		phones.add("999-999-999");
		phones.add("888-888-888");
		phones.add("777-777-777");
		phones.add("666-666-666");
		phones.add("555-555-555");

		when(personsInfosDaoI.findPhoneByStationNumber(1)).thenReturn(phones);
		
		// ACT
		phoneAlertDto = personsInfosServiceImpl.getListPhoneByStation(1);
		
		// ASSERT
		assertThat(phoneAlertDto.getPhones().size()).isEqualTo(5);
		verify(personsInfosDaoI).findPhoneByStationNumber(1);
	}
	
	@Test
	public void testGetPeopleByAddress() {
		// ARANGE
		StreetDto streetDto = new StreetDto();
		
		when(personsInfosDaoI.findPersonsByAddress("1509 Culver St")).thenReturn(person);
		when(personsInfosDaoI.findMedicalRecordsByListPerson(person)).thenReturn(medicalRecordList);
		
		// ACT
		streetDto = personsInfosServiceImpl.getPeopleByAddress("1509 Culver St");
		
		// ASSERT
		assertThat(streetDto.getStreetPeopleDto().size()).isEqualTo(5);
		verify(personsInfosDaoI).findPersonsByAddress("1509 Culver St");
		verify(personsInfosDaoI).findMedicalRecordsByListPerson(person);
		}

	@Test
	public void testGetPeopleByListStation() {
		// ARRANGE
		//StationsDto stationDto = new StationsDto();
		List<StationsDto> stationsListDto = new ArrayList<>();
		List<Integer> stationsNumbers = new ArrayList<>();
		stationsNumbers.add(1);
		stationsNumbers.add(2);
		stationsNumbers.add(2);
		when(personsInfosDaoI.findPersonsByStation(stationsNumbers)).thenReturn(person);
		when(personsInfosDaoI.findMedicalRecordsByListPerson(person)).thenReturn(medicalRecordList);
		
		// ACT
		stationsListDto = personsInfosServiceImpl.getPeopleByListStation(stationsNumbers);
		log.debug("stationsList : " + stationsListDto);
		
		// ASSERT
		assertThat(stationsListDto.toString()).contains("Tessa");
		verify(personsInfosDaoI).findPersonsByStation(stationsNumbers);
		verify(personsInfosDaoI).findMedicalRecordsByListPerson(person);
	}
	
	@Test
	public void testGetPersonInfo() {
		// ARRANGE
		PersonInfoDto personInfo = new PersonInfoDto();
		when(personsInfosDaoI.findPersonByFistNameAndLastName("Tessa", "Carman")).thenReturn(personTest3);
		when(personsInfosDaoI.findMedicalRecordsByPerson(personTest3)).thenReturn(medTest3);
		// ACT
		personInfo = personsInfosServiceImpl.getPersonInfo("Tessa", "Carman");
		// ASSERT
		assertThat(personInfo.getOld()).isEqualTo(11);
		assertThat(personInfo.getMedications()).contains("dolipranne");
		assertThat(personInfo.getAllergies()).contains("noisette");

		verify(personsInfosDaoI).findPersonByFistNameAndLastName("Tessa", "Carman");
		verify(personsInfosDaoI).findMedicalRecordsByPerson(personTest3);
	}
	
	@Test
	public void testGetCommunityEmail() {
		// ARRANGE
		CommunityEmailDto emails = new CommunityEmailDto();
		List<String> email = new ArrayList<>();
		email.add(personTest1.getEmail());
		email.add(personTest2.getEmail());
		email.add(personTest3.getEmail());
		email.add(personTest4.getEmail());
		email.add(personTest5.getEmail());

		when(personsInfosDaoI.findEmailByCity("culver")).thenReturn(email);
		
		// ACT
		emails = personsInfosServiceImpl.getCommunityEmail("culver");
		// ASSERT
		assertThat(emails.getEmails().size()).isEqualTo(5);
		assertThat(emails.getEmails()).contains("jaboyd@email.com");
		verify(personsInfosDaoI).findEmailByCity("culver");
	}
	
	@Test
	public void simpleEqualsContract() {
	    EqualsVerifier.simple().forClass(PersonsInfosServiceImpl.class).verify();
	}
	
	@Test
	public void testToString()
	{
	    ToStringVerifier.forClass(PersonsInfosServiceImpl.class)
	                    .withClassName(NameStyle.SIMPLE_NAME)
	                    .verify();
	}
}
