package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.alerts.dao.MedicalRecordDaoI;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(MedicalRecordServiceImpl.class)
class MedicalRecordServiceTest {

	@MockBean
	MedicalRecordDaoI medicalRecordDaoI;
	
	@Autowired
	MedicalRecordServiceImpl medicalRecordServiceImpl;
	
	MedicalRecord medTest1 = new MedicalRecord();
	MedicalRecord medTest2 = new MedicalRecord();
	MedicalRecord medTest3 = new MedicalRecord();
	MedicalRecord medTest4 = new MedicalRecord();
	MedicalRecord medTest5 = new MedicalRecord();
	List<MedicalRecord> medicalRecord = new ArrayList<>();
	
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
		
		medicalRecord.add(medTest1);		
		medicalRecord.add(medTest2);
		medicalRecord.add(medTest3);
		medicalRecord.add(medTest4);
		medicalRecord.add(medTest5);

	}
	
	@Test
	void testGetListMedicalRecords() {
		// ARRANGE
		List<MedicalRecord> listeAllMedical = new ArrayList<>();
		when(medicalRecordDaoI.findAllMedicalRecords()).thenReturn(medicalRecord);
		// ACT 
		listeAllMedical = medicalRecordServiceImpl.getListMedicalRecords();
		// ASSERT
		assertThat(listeAllMedical.size()).isEqualTo(5);
	}
	
	@Test
	void testGetMedicalRecord() {
		// ARRANGE
		MedicalRecord medRecord = new MedicalRecord();
		when(medicalRecordDaoI.findMedicalRecordByFirstNameAndLastName("Aurelia", "Marchand")).thenReturn(medTest4);
		//ACT
		medRecord = medicalRecordServiceImpl.getMedicalRecord("Aurelia", "Marchand");
		// ASSERT
		assertThat(medRecord.getBirthdate()).isEqualTo("03/11/2010");
	}
	
	@Test
	void testDeleteMedicalRecord() {
		// ARRANGE
		MedicalRecord medRecord1 = new MedicalRecord();
		when(medicalRecordDaoI.findMedicalRecordByFirstNameAndLastName("Aurelia", "Marchand")).thenReturn(medTest4);
		//ACT
		medRecord1 = medicalRecordServiceImpl.deleteMedicalRecord("Aurelia", "Marchand");
		// ASSERT
		assertThat(medRecord1).isEqualTo(medTest4);
		}
	
	@Test
	void testPutMedicalRecord() {
		// ARRANGE
		List<String> medications1 = new ArrayList<>();
		List<String> allergies1 = new ArrayList<>();
		medications1.add("dolipranne");
		allergies1.add("oeuf");
		MedicalRecord medTest6 = new MedicalRecord("Aurelia", "Marchand", "04/13/1980", medications1, allergies1);
		MedicalRecord medRecord1 = new MedicalRecord();
		when(medicalRecordDaoI.findMedicalRecordByFirstNameAndLastName("Aurelia", "Marchand")).thenReturn(medTest4);
		when(medicalRecordDaoI.updateMedicalRecord(medTest6)).thenReturn(medTest6);
		
		//ACT
		medRecord1 = medicalRecordServiceImpl.putMedicalRecord(medTest6);
		// ASSERT
		assertThat(medRecord1.getAllergies()).contains("oeuf");
		}
	
	@Test
	void testPostMedicalRecord() {
		// ARRANGE
		List<String> medications = new ArrayList<>();
		List<String> allergies = new ArrayList<>();
		medications.add("dolipranne");
		allergies.add("oeuf");
		MedicalRecord mediRecordToPost = new MedicalRecord();
		MedicalRecord newMedRecord = new MedicalRecord("nouvelle", "Personne", "10/10/2010", medications, allergies);
		when(medicalRecordDaoI.findMedicalRecordByFirstNameAndLastName("Nouvelle", "Personne")).thenReturn(null);
		
		// ACT
		mediRecordToPost = medicalRecordServiceImpl.postMedicalRecord(newMedRecord);
		
		// ASSERT
		assertThat(mediRecordToPost.getAllergies()).contains("oeuf");
		verify(medicalRecordDaoI).saveMedicalRecord(newMedRecord);
	}

}
