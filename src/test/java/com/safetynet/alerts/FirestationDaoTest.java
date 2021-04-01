package com.safetynet.alerts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.safetynet.alerts.dao.AccessJsonI;
import com.safetynet.alerts.dao.Datas;
import com.safetynet.alerts.dao.FirestationDaoImpl;
import com.safetynet.alerts.model.Firestation;

import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;

@WebMvcTest(FirestationDaoImpl.class)
@Slf4j
class FirestationDaoTest {

	@Autowired
	FirestationDaoImpl firestationDaoImpl;
	
	@MockBean
	AccessJsonI accessJsonI;
	
	List<Firestation> firestations = new ArrayList<>();
	Datas personsInfos = new Datas();
	
	@BeforeEach
	private void setUpPerTest() {
		Firestation f1 = new Firestation();
		f1.setAddress("rue de la plaine");
		f1.setStation(1);
		Firestation f2 = new Firestation();
		f2.setAddress("rue du val");
		f2.setStation(2);
		Firestation f3 = new Firestation();
		f3.setAddress("rue des montagnes");
		f3.setStation(3);
		firestations.add(f1);
		firestations.add(f2);
		firestations.add(f3);

		personsInfos.setFirestations(firestations);
	}
	
	@Test
	void testFindAllFirestations() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		List<Firestation> result = firestationDaoImpl.findAllFirestations();
		// ASSERT
		assertThat(result.size()).isEqualTo(3);
	}
	
	@Test
	void testGetFirestation() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		Firestation result = firestationDaoImpl.findFirestationByAddress("rue de la plaine");
		// ASSERT
		assertThat("rue de la plaine").isEqualToIgnoringCase(result.getAddress());
	}
	
	@Test
	void testSaveFirestation() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		Firestation firestationToPost = new Firestation();
		firestationToPost.setAddress("allée du lac");
		firestationToPost.setStation(4);

		// ACT
		Firestation newFirestation = firestationDaoImpl.saveFirestation(firestationToPost);

		// ASSERT
		assertThat(personsInfos.getFirestations()).contains(newFirestation);
		firestations = personsInfos.getFirestations();
		assertThat(firestations.size()).isEqualTo(4);
	}
	
	@Test
	void testUpdateFirestation() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		Firestation firestationToPut = new Firestation();
		firestationToPut.setAddress("rue des montagnes");
		firestationToPut.setStation(1);

		// ACT
		firestationDaoImpl.updateFirestation(firestationToPut);

		// ASSERT
		List<Firestation> firestations = personsInfos.getFirestations();
		assertThat(firestations).contains(firestationToPut);
		assertThat(firestationToPut.getAddress()).contains("rue des montagnes");
		assertThat(firestationToPut.getStation()).isEqualTo(1);


		for (Firestation firestation : firestations) {
			if (firestation.getAddress() == "rue des montagnes") {
				assertThat(firestation.getStation()).isEqualTo(1);
			}
		}
	}
	
	@Test
	void testDeleteFirestation() {
		// ARRANGE
		when(accessJsonI.getData()).thenReturn(personsInfos);
		// ACT
		firestationDaoImpl.deleteFirestationByAddress("rue de la plaine");
		List<Firestation> result = personsInfos.getFirestations();
		// ASSERT
		assertThat(2).isEqualTo(result.size());
	}
	
	@Test
	public void simpleEqualsContract() {
		EqualsVerifier.simple().forClass(FirestationDaoImpl.class).verify();
	}

	@Test
	public void testToString() {
		ToStringVerifier.forClass(FirestationDaoImpl.class).withClassName(NameStyle.SIMPLE_NAME).verify();
	}

}
