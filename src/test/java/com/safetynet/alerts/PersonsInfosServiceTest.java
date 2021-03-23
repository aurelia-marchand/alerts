package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.safetynet.alerts.dao.PersonsInfosDaoImpl;
import com.safetynet.alerts.dto.PeopleCoveredDto;
import com.safetynet.alerts.dto.PersonsByStationDto;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.PersonsInfosServiceImpl;

import nl.jqno.equalsverifier.EqualsVerifier;

@WebMvcTest(PersonsInfosServiceImpl.class)
@RunWith(MockitoJUnitRunner.class)
class PersonsInfosServiceTest {
	
	@MockBean
	PersonsInfosDaoImpl personsInfosDaoImpl;
	
	@Autowired
	PersonsInfosServiceImpl personsInfosServiceImpl;
	
	@MockBean
	PeopleCoveredDto peopleCoveredDto;
	
	@MockBean
	PersonsByStationDto personsByStationDto;
	
	List<MedicalRecord> medicalRecordList = new ArrayList<>();
	
	
	@BeforeEach
	private void setUpPerTest() {
		List<String> medications = new ArrayList<>();
		List<String> allergies = new ArrayList<>();
		medications.add("dolipranne");
		allergies.add("noisette");
		
		MedicalRecord personTest1 = new MedicalRecord("Felicia", "Boyd", "03/11/2010", medications, allergies);
		MedicalRecord personTest2 = new MedicalRecord("Felicia", "Boyd", "03/11/2010", medications, allergies);
		MedicalRecord personTest3 = new MedicalRecord("Felicia", "Boyd", "03/11/2010", medications, allergies);
		MedicalRecord personTest4 = new MedicalRecord("Felicia", "Boyd", "03/11/2010", medications, allergies);

		MedicalRecord personTest5 = new MedicalRecord("Jonanathan", "Marrack", "12/29/1976", medications, allergies);
		
		
		medicalRecordList.add(personTest1);		
		medicalRecordList.add(personTest2);
		medicalRecordList.add(personTest3);
		medicalRecordList.add(personTest4);
		medicalRecordList.add(personTest5);
	}

	@Test
	void TestcalculateNumberOfChildren(){
		
		int result = PersonsInfosServiceImpl.calculateNumberOfChildren(medicalRecordList);
		
		assertThat(result).isEqualTo(4);

	}
	
	@Test
	void TestcalculateNumberOfAdult(){
		
		int result = PersonsInfosServiceImpl.calculateNumberOfAdults(medicalRecordList);
		
		assertThat(result).isEqualTo(1);

	}
	
	@Test
	void TestCalculateAge() {
		String birthdate = "04/13/1984";
		
		int result = PersonsInfosServiceImpl.calculateAge(birthdate);
		
		assertThat(result).isEqualTo(36);
	}
	
	@Disabled
	@Test
	void testGetListPersonsByStationNumber() {
		PersonsByStationDto personsByStationDto = new PersonsByStationDto();
		List<PersonsByStationDto> list = new ArrayList<>();
		list.add(personsByStationDto);
		
		personsInfosServiceImpl.getListPersonsByStationNumber(2);
		
		verify(personsInfosDaoImpl).findPersonsByStationNumber(2);
		//verify(personsInfosDaoImpl).findMedicalRecordsByPersons(list);
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
