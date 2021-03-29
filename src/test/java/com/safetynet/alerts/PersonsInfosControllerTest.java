package com.safetynet.alerts;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.controller.PersonsInfosController;
import com.safetynet.alerts.dto.AlertPhoneDto;
import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.CommunityEmailDto;
import com.safetynet.alerts.dto.DistrictDto;
import com.safetynet.alerts.dto.DistrictPersonsDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.StationsDto;
import com.safetynet.alerts.dto.StreetDto;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonsInfosServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(controllers = PersonsInfosController.class)
class PersonsInfosControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PersonsInfosServiceI personsInfosService;
	
	MedicalRecord med1 = new MedicalRecord();
	MedicalRecord med2 = new MedicalRecord();
	MedicalRecord med3 = new MedicalRecord();
	List<MedicalRecord> medRecords = new ArrayList<>();
	
	Person personTest1 = new Person();
	Person personTest2 = new Person();
	Person personTest3 = new Person();
	Person personTest4 = new Person();
	List<Person> persons = new ArrayList<>();
	DistrictDto districtDto = new DistrictDto();


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
		med3.setMedications(new ArrayList<>(Arrays.asList("\"hydrapermazol:300mg\", \"dodoxadin:30mg")));
		med3.setAllergies(new ArrayList<>(Arrays.asList("shellfish")));
		
		medRecords.add(med1);
		medRecords.add(med2);
		medRecords.add(med3);

		personTest1 = new Person("Felicia", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6544",
				"jaboyd@email.com");
		personTest2 = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513",
				"drk@email.comm");
		personTest3 = new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", 97451, "841-874-6512", "tenz@email.com");
		personTest4 = new Person("Aurélia", "Marchand", "place de la halle", "magny", 95420, "999-999-999", "aure@email.com");
		persons.add(personTest1);
		persons.add(personTest2);
		persons.add(personTest3);
		
		DistrictPersonsDto districtPersonDto1 = new DistrictPersonsDto();
		ModelMapper modelMapper = new ModelMapper();
		districtPersonDto1 = modelMapper.map(personTest1, DistrictPersonsDto.class);
		DistrictPersonsDto districtPersonDto2 = new DistrictPersonsDto();
		ModelMapper modelMapper2 = new ModelMapper();
		districtPersonDto2 = modelMapper2.map(personTest2, DistrictPersonsDto.class);
		DistrictPersonsDto districtPersonDto3 = new DistrictPersonsDto();
		ModelMapper modelMapper3 = new ModelMapper();
		districtPersonDto3 = modelMapper3.map(personTest3, DistrictPersonsDto.class);

		
		List<DistrictPersonsDto> districtPersonsDto = new ArrayList<>();
		districtPersonsDto.add(districtPersonDto1);
		districtPersonsDto.add(districtPersonDto2);
		districtPersonsDto.add(districtPersonDto3);

		
		districtDto.setDistrictPersonsDto(districtPersonsDto);
		districtDto.setNumberOfAdults(2);
		districtDto.setNumberOfChildren(1);


	}
	
	
	@Test
	void testGetPersonsByStationIfExist() throws Exception {
		when(personsInfosService.getListPersonsByStationNumber(1)).thenReturn(districtDto);
		
		mockMvc.perform(get("/firestation?stationNumber=1")).andExpect(status().isOk());
	}
	
	@Test
	void testGetPersonsByStationIfNotExist() throws Exception {
		DistrictDto districtDto2 = new DistrictDto();
		when(personsInfosService.getListPersonsByStationNumber(10)).thenReturn(districtDto2);
		
		mockMvc.perform(get("/firestation?stationNumber=10")).andExpect(status().isNotFound());
	}

	@Test
	void testGetChidrenByAddressIfExist() throws Exception {
		ChildAlertDto childAlertDto = new ChildAlertDto();
		when(personsInfosService.getListChildrenByAddress("1509 Culver St")).thenReturn(childAlertDto);
		
		mockMvc.perform(get("/childAlert?address=1509 Culver St")).andExpect(status().isOk());
	}
	
	@Test
	void testGetChidrenByAddressIfNotExist() throws Exception {
		ChildAlertDto childAlertDto = null;
		when(personsInfosService.getListChildrenByAddress("place de la halle")).thenReturn(childAlertDto);
		
		mockMvc.perform(get("/childAlert?address=1509 Culver St")).andExpect(status().isNotFound());
	}
	
	@Test
	void testGetPhoneByStationNumberIfExist() throws Exception {
		AlertPhoneDto alertPhoneDto = new AlertPhoneDto();
		when(personsInfosService.getListPhoneByStation(1)).thenReturn(alertPhoneDto);
		
		mockMvc.perform(get("/phoneAlert?firestation=1")).andExpect(status().isOk());
	}
	
	@Test
	void testGetPhoneByStationNumberIfNotExist() throws Exception {
		when(personsInfosService.getListPhoneByStation(2)).thenReturn(null);
		
		mockMvc.perform(get("/phoneAlert?firestation=1")).andExpect(status().isNotFound());
	}
	
	
	@Test
	void testGetPeopleByAddressIfExist() throws Exception {
		StreetDto streetDto = new StreetDto();
		when(personsInfosService.getPeopleByAddress("1509 Culver St")).thenReturn(streetDto);
		
		mockMvc.perform(get("/fire?address=1509 Culver St")).andExpect(status().isOk());
	}
	
	@Test
	void testGetPeopleByAddressIfNotExist() throws Exception {
		when(personsInfosService.getPeopleByAddress("1509 Culver St")).thenReturn(null);
		
		mockMvc.perform(get("/fire?address=1509 Culver St")).andExpect(status().isNotFound());
	}
	
	@Test
	void testGetPeopleByStationIfExist() throws Exception {
		List<StationsDto> stationsDto = new ArrayList<>(); 
		List<Integer> stations = new ArrayList<>();
		stations.add(1);
		stations.add(2);
		
		when(personsInfosService.getPeopleByListStation(stations)).thenReturn(stationsDto);
		
		mockMvc.perform(get("/flood/stations?stations=1,2")).andExpect(status().isOk());
	}
	
	@Test
	void testGetPeopleByStationIfNotExist() throws Exception {
		
		List<Integer> stations = new ArrayList<>();
		stations.add(1);
		stations.add(2);
		
		when(personsInfosService.getPeopleByListStation(stations)).thenReturn(null);
		
		mockMvc.perform(get("/flood/stations?stations=1,2")).andExpect(status().isNotFound());
	}
	
	@Test
	void testGetPersonInfoIfExist() throws Exception {
		PersonInfoDto personInfo = new PersonInfoDto();
		
		when(personsInfosService.getPersonInfo("felicia", "boyd")).thenReturn(personInfo);
		
		mockMvc.perform(get("/personInfo?firstName=felicia&lastName=boyd")).andExpect(status().isOk());
	}
	
	@Test
	void testGetPersonInfoIfNotExist() throws Exception {
		
		when(personsInfosService.getPersonInfo("anna", "boyd")).thenReturn(null);
		
		mockMvc.perform(get("/personInfo?firstName=anna&lastName=boyd")).andExpect(status().isNotFound());
	}
	
	@Test
	void testGetCommunityEmailIfExist() throws Exception {
		CommunityEmailDto emailListDto = new CommunityEmailDto();		
		when(personsInfosService.getCommunityEmail("culver")).thenReturn(emailListDto);
		
		mockMvc.perform(get("/communityEmail?city=culver")).andExpect(status().isOk());
	}
	
	@Test
	void testGetCommunityEmailIfNotExist() throws Exception {
		when(personsInfosService.getCommunityEmail("culver")).thenReturn(null);
		
		mockMvc.perform(get("/communityEmail?city=culver")).andExpect(status().isNotFound());
	}
	
	
}
