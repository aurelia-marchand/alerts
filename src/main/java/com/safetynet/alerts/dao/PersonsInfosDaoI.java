package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.dto.PersonsByStationDto;
import com.safetynet.alerts.model.MedicalRecord;

public interface PersonsInfosDaoI {
	
	public List<PersonsByStationDto> findPersonsByStationNumber(int station);

	public List<MedicalRecord> findMedicalRecordsByPersons(List<PersonsByStationDto> personsByStationDto2);
}
