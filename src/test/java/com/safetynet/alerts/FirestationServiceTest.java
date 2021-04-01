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

import com.safetynet.alerts.dao.FirestationDaoI;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(FirestationServiceImpl.class)
class FirestationServiceTest {
	
	@MockBean
	FirestationDaoI firestationDaoI;
	
	
	@Autowired
	FirestationServiceImpl firestationServiceImpl;

	List<Firestation> firestations = new ArrayList<>();
	Firestation firestation1 = new Firestation();
	Firestation firestation2 = new Firestation();
	Firestation firestation3 = new Firestation();
	Firestation firestation4 = new Firestation();
	
	@BeforeEach
	private void setUpPerTest() {
		
		firestation1 = new Firestation();
		firestation1.setAddress("rue du val");
		firestation1.setStation(1);
		firestation2 = new Firestation();
		firestation2.setAddress("place de la halle");
		firestation2.setStation(2);
		firestation3 = new Firestation();
		firestation3.setAddress("rue du général");
		firestation3.setStation(3);
		firestation4 = new Firestation();
		firestation4.setAddress("avenue de la paix");
		firestation4.setStation(4);

		firestations.add(firestation1);		
		firestations.add(firestation2);
		firestations.add(firestation3);
		firestations.add(firestation4);

	}
	
	@Test
	void testGetFirestation() {
		//ARRANGE
		Firestation firestationToGet = new Firestation();
		when(firestationDaoI.findFirestationByAddress("rue du val")).thenReturn(firestation1);
		//ACT
		firestationToGet = firestationServiceImpl.getFirestation("rue du val");
		//ASSERT
		assertThat(firestationToGet.getStation()).isEqualTo(firestation1.getStation());
		}
	
	@Test
	void testPostFirestation() {
		//ARRANGE
		Firestation firestationToPost = new Firestation();
		firestationToPost.setAddress("allée de la plaine");
		firestationToPost.setStation(6);
		
		when(firestationDaoI.findFirestationByAddress("allée de la plaine")).thenReturn(null);
		when(firestationDaoI.saveFirestation(firestationToPost)).thenReturn(firestation1);
		//ACT
		Firestation newFirestation = firestationServiceImpl.postFirestation(firestationToPost);
		//ASSERT
		assertThat(newFirestation.getStation()).isEqualTo(6);
		verify(firestationDaoI).saveFirestation(firestationToPost);
		}
	
	@Test
	void testPutFirestation() {
		//ARRANGE
		Firestation firestationToPut = new Firestation();
		firestationToPut.setAddress("allée de la plaine");
		firestationToPut.setStation(6);
		
		when(firestationDaoI.findFirestationByAddress("allée de la plaine")).thenReturn(firestation1);
		when(firestationDaoI.updateFirestation(firestation1)).thenReturn(firestationToPut);
		//ACT
		Firestation updateFirestation = firestationServiceImpl.putFirestation(firestationToPut);
		//ASSERT
		assertThat(updateFirestation.getStation()).isEqualTo(6);
		}
	
	@Test
	void testDeleteFirestation() {
		//ARRANGE
		when(firestationDaoI.findFirestationByAddress("place de la halle")).thenReturn(firestation2);
		when(firestationDaoI.deleteFirestationByAddress("place de la halle")).thenReturn(firestation2);
		//ACT
		Firestation deledtedFirestation = firestationServiceImpl.deleteFirestation("place de la halle");
		//ASSERT
		assertThat(deledtedFirestation).isNull();
		}

	@Test
	void testGetListFirestations() {
		//ARRANGE
		when(firestationDaoI.findAllFirestations()).thenReturn(firestations);
		//ACT
		List<Firestation> fireList = firestationServiceImpl.GetListFirestations();
		//ASSERT
		assertThat(fireList.size()).isEqualTo(4);
	}

}
