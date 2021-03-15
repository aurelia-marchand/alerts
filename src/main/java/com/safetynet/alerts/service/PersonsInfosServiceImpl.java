package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dao.PersonsInfosDaoI;
import com.safetynet.alerts.dto.PeopleCoveredDto;
import com.safetynet.alerts.dto.PersonsByStationDto;
import com.safetynet.alerts.model.MedicalRecord;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class PersonsInfosServiceImpl implements PersonsInfosServiceI{

	@Autowired
	PersonsInfosDaoI personsInfosDao;
	@Autowired
	PeopleCoveredDto peopleCoveredDto;

	@Override
	public PeopleCoveredDto GetListPersonsByStationNumber(int station) {

		// On récupère les personnes couvertes par la station via les méthodes de l'interface Dao
		List<PersonsByStationDto> personsByStationDto = personsInfosDao.findPersonsByStationNumber(station);
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByPersons(personsByStationDto);
		
		// On initialise une liste pour pouvoir y stocker nos résultats
		List<MedicalRecord> listeMedicalRecord = new ArrayList<>();

		// Boucle pour récupérer les données médicales des personnes couvertes par la station
		for (MedicalRecord medicalRecord : medicalRecords) {
			for (PersonsByStationDto personByStationDto : personsByStationDto) {
				if (personByStationDto.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
						&& personByStationDto.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
					
					listeMedicalRecord.add(medicalRecord);
				}
			}
		}
		// On Complète notre Dto avant de le renvoyer
		peopleCoveredDto.setNumberOfAdults(calculateNumberOfAdults(listeMedicalRecord));
		peopleCoveredDto.setNumberOfChildren(calculateNumberOfChildren(listeMedicalRecord));
		peopleCoveredDto.setPersonsByStationDto(personsByStationDto);

		return peopleCoveredDto;
	}

	private int calculateNumberOfChildren(List<MedicalRecord> listeMedicalRecord) {
		Integer numberOfChildren = 0;
		List<MedicalRecord> listeMedicalRecords = listeMedicalRecord;
		for (MedicalRecord medicalRecord : listeMedicalRecords) {
				int age = calculateAge(medicalRecord.getBirthdate());
				if (age <= 18) {
					numberOfChildren++;
				}
		}
		return numberOfChildren;
	}

	private int calculateNumberOfAdults(List<MedicalRecord> listeMedicalRecord) {
		Integer numberOfAdults = 0;
		List<MedicalRecord> listeMedicalRecords = listeMedicalRecord;
		for (MedicalRecord medicalRecord : listeMedicalRecords) {
				int age = calculateAge(medicalRecord.getBirthdate());
				if (age > 18) {
					numberOfAdults++;
			}
		}
		return numberOfAdults;
	}

	private int calculateAge(String birthdate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		LocalDate date = LocalDate.parse(birthdate, formatter);

		LocalDate birthDate = date;
		LocalDate currentDate = LocalDate.now();
		return Period.between(birthDate, currentDate).getYears();
	}

}
