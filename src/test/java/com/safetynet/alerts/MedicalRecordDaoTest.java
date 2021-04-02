package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.alerts.dao.AccessJsonI;
import com.safetynet.alerts.dao.Datas;
import com.safetynet.alerts.dao.MedicalRecordDaoI;
import com.safetynet.alerts.dao.MedicalRecordDaoImpl;
import com.safetynet.alerts.model.MedicalRecord;

@WebMvcTest(MedicalRecordDaoImpl.class)
class MedicalRecordDaoTest {

	@Autowired
	MedicalRecordDaoI medicalRecordDaoI;

	@MockBean
	AccessJsonI accessJsonI;

	List<MedicalRecord> medicalRecords = new ArrayList<>();
	Datas personsInfos = new Datas();

	MedicalRecord med1 = new MedicalRecord();
	MedicalRecord med2 = new MedicalRecord();
	MedicalRecord med3 = new MedicalRecord();

	@BeforeEach
	private void setUpPerTest() {
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

		medicalRecords.add(med1);
		medicalRecords.add(med2);
		medicalRecords.add(med3);

		personsInfos.setMedicalrecords(medicalRecords);
	}

	@Test
	void testFindAllMedicalRecord() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		List<MedicalRecord> result = medicalRecordDaoI.findAllMedicalRecords();
		// ASSERT
		assertThat(result.size()).isEqualTo(3);
	}

	// TODO PAS VERIFIER A PARTIR DE LA
	@Test
	void testFindMedicalRecordByFirstNameAndLastName() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		MedicalRecord result = medicalRecordDaoI.findMedicalRecordByFirstNameAndLastName("Felicia", "Boyd");
		// ASSERT
		assertThat("Felicia").isEqualToIgnoringCase(result.getFirstName());
		assertThat("tetracyclaz:650mg").contains(result.getMedications());
	}

	// TODO REPRENDRE ICI
	@Test
	void testSaveMedicalRecord() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		MedicalRecord medToPost = new MedicalRecord();
		medToPost.setFirstName("anna");
		medToPost.setFirstName("dubois");
		medToPost.setBirthdate("06/11/1986");

		// ACT
		MedicalRecord newMedicalRecord = medicalRecordDaoI.saveMedicalRecord(medToPost);

		// ASSERT
		assertThat(personsInfos.getMedicalrecords()).contains(newMedicalRecord);
	}

	@Test
	void testUpdateMedicalRecord() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		MedicalRecord medToPut = new MedicalRecord();
		List<String> medications = new ArrayList<>();
		medications.add("dolipranne, 500mg");
		List<String> allergies = new ArrayList<>();
		allergies.add("gluten");
		medToPut.setFirstName("Felicia");
		medToPut.setLastName("Boyd");
		medToPut.setMedications(medications);
		medToPut.setAllergies(allergies);

		// ACT
		medicalRecordDaoI.updateMedicalRecord(medToPut);

		// ASSERT
		List<MedicalRecord> medicalRecords = personsInfos.getMedicalrecords();
		assertThat(medicalRecords).contains(medToPut);
		assertThat(medToPut.getMedications()).contains("dolipranne, 500mg");

		for (MedicalRecord med : medicalRecords) {
			if (med.getFirstName() == "Felicia") {
				assertThat(med.getAllergies()).contains("gluten");
			}
		}
	}

	@Test
	void testDeleteMedicalRecord() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		medicalRecordDaoI.deleteMedicalRecordByFirstNameAndLastName("Jonhathan", "Marrack");
		List<MedicalRecord> result = personsInfos.getMedicalrecords();
		// ASSERT
		assertThat(2).isEqualTo(result.size());
	}

}
