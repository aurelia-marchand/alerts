package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dao.PersonsInfosDaoI;
import com.safetynet.alerts.dto.AlertPhoneDto;
import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.ChildrenByAddressDto;
import com.safetynet.alerts.dto.ChildrenFamilyDto;
import com.safetynet.alerts.dto.CommunityEmailDto;
import com.safetynet.alerts.dto.DistrictDto;
import com.safetynet.alerts.dto.DistrictPeopleDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.PersonStationsDto;
import com.safetynet.alerts.dto.StationsDto;
import com.safetynet.alerts.dto.StationsPeopleByAddressDto;
import com.safetynet.alerts.dto.StreetDto;
import com.safetynet.alerts.dto.StreetPeopleDto;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class PersonsInfosServiceImpl implements PersonsInfosServiceI {

	@Autowired
	PersonsInfosDaoI personsInfosDao;
	

	@Override
	public DistrictDto getListPersonsByStationNumber(int station) {

		
		
		// Récupération des personnes couvertes par la station
		List<Person> persons = personsInfosDao.findPersonsByStationNumber(station);
		

		// Préparation de la liste et des entités pour stocker les personnes avec uniquement les
		// valeurs qu'on veut renvoyer
		List<DistrictPeopleDto> listePeopleDto = new ArrayList<>();
		DistrictDto districtDto= new DistrictDto();
		DistrictPeopleDto districtPersonsDto = new DistrictPeopleDto();

		for (Person person : persons) {
			ModelMapper modelMapper = new ModelMapper();
			districtPersonsDto = modelMapper.map(person, DistrictPeopleDto.class);
			listePeopleDto.add(districtPersonsDto);
		}

		// Récupération des données médicales des personnes sur notre liste persons
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByListPerson(persons);

		// On Complète notre Dto avant de le renvoyer
		districtDto.setNumberOfAdults(calculateNumberOfAdults(medicalRecords));
		log.debug("Calcul du nombre d'adultes : " + districtDto.getNumberOfAdults());
		districtDto.setNumberOfChildren(calculateNumberOfChildren(medicalRecords));
		log.debug("Calcul du nombre d'enfants : " + districtDto.getNumberOfChildren());
		districtDto.setDistrictPersonsDto(listePeopleDto);

		return districtDto;
	}

	@Override
	public ChildAlertDto getListChildrenByAddress(String address) {
		// Récupération données personnes et médicales
		List<Person> personneByAddress = personsInfosDao.findPersonsByAddress(address);
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByListPerson(personneByAddress);

		// Préparation des entités de stockage à renvoyer
		List<ChildrenFamilyDto> familyMembersDto = new ArrayList<>();
		ChildrenFamilyDto FamilyMember = new ChildrenFamilyDto();
		ChildrenByAddressDto childrenByAddressDto = new ChildrenByAddressDto();
		List<ChildrenByAddressDto> childrenByAddress = new ArrayList<>();
		ChildAlertDto childAlert = new ChildAlertDto();

		// Boucle pour lier personnes et leurs dossiers medicaux, calculer leurs âges et les
		// placer soit comme enfant soit comme membres de la famille
		for (Person person : personneByAddress) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())
						&& medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())) {
					int age = calculateAge(medicalRecord.getBirthdate());
					if (age <= 18) {
						// Utilisation ModelMapper pour map Dto/entité
						ModelMapper modelMapper = new ModelMapper();
						childrenByAddressDto = modelMapper.map(person, ChildrenByAddressDto.class);
						childrenByAddressDto.setAge(age);
						childrenByAddress.add(childrenByAddressDto);
					} else {
						ModelMapper modelMapper = new ModelMapper();
						FamilyMember = modelMapper.map(person, ChildrenFamilyDto.class);
						FamilyMember.setAge(age);
						familyMembersDto.add(FamilyMember);
					}
				}
			}
			childAlert.setChildrenByAdress(childrenByAddress);
			childAlert.setChildrenFamilyDto(familyMembersDto);
		}

		if (childAlert.getChildrenByAdress() == null && childAlert.getChildrenFamilyDto() == null) {
			childAlert = null;
		}
		return childAlert;
	}

	@Override
	public AlertPhoneDto getListPhoneByStation(int station) {
		// Récupération des numéros de téléphones par station
		List<String> phones = personsInfosDao.findPhoneByStationNumber(station);
		
		// Préparation entité de stockage et tranfert
		AlertPhoneDto phoneAlertDto = new AlertPhoneDto();

		phoneAlertDto.setPhones(phones);
		log.debug("phoneAlertDto" + phoneAlertDto);

		if (phones.size() == 0) {
			phoneAlertDto = null;
		}

		return phoneAlertDto;
	}

	@Override
	public StreetDto getPeopleByAddress(String address) {
		// Récupération des personnes à l'adresse et de leurs dossiers médicaux
		List<Person> peopleByAddress = personsInfosDao.findPersonsByAddress(address);
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByListPerson(peopleByAddress);
		//Récupération du numéro de la station
		int station = personsInfosDao.findStationByAddress(address);

		// Préparation entités de transfert
		List<String> medications = new ArrayList<>();
		List<String> allergies = new ArrayList<>();
		StreetPeopleDto peopleFireDto = new StreetPeopleDto();
		List<StreetPeopleDto> peoplesFireDto = new ArrayList<>();
		StreetDto streetDto = new StreetDto();

		// Boucle association personnes/dossiers médicaux
		for (Person personByAddress : peopleByAddress) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (personByAddress.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
						&& personByAddress.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
					medications = medicalRecord.getMedications();
					allergies = medicalRecord.getAllergies();

					ModelMapper modelMapper = new ModelMapper();
					peopleFireDto = modelMapper.map(personByAddress, StreetPeopleDto.class);
					peopleFireDto.setAge(calculateAge(medicalRecord.getBirthdate()));
					peopleFireDto.setMedications(medications);
					peopleFireDto.setAllergies(allergies);

					peoplesFireDto.add(peopleFireDto);
				}
			}
		}
		streetDto.setStreetPeopleDto(peoplesFireDto);
		streetDto.setStation(station);

		if (streetDto.getStreetPeopleDto() == null && streetDto.getStation() == 0) {
			streetDto = null;
		}

		return streetDto;
	}

	@Override
	public List<StationsDto> getPeopleByListStation(List<Integer> stationsList) {

		List<Integer> stations = stationsList;

		// On récupère les personnes des stations
		List<Person> persons = personsInfosDao.findPersonsByStation(stations);

		// On récupère les dossiers médicaux des personnes concernées
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByListPerson(persons);

		// On prépare deux listes pour stocker les antécédents médicaux (medicaments,
		// posologie et allergies)
		List<String> medications = new ArrayList<>();
		List<String> allergies = new ArrayList<>();

		// On crée une entité personne
		PersonStationsDto personByAddressDto = new PersonStationsDto();
		// On prépare la liste pour stocker les personnes à afficher
		List<PersonStationsDto> personsStationsDto = new ArrayList<>();

		// On prepare un hashset pour stocker les adresses sans doublons.
		Set<String> adresses = new HashSet<>();

		// Boucle pour faire correspondre les personnes et leurs dossiers
		for (Person person : persons) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
						&& person.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {

					adresses.add(person.getAddress());
					// on intègre la personne, ses données médicales et son âge dans l'entité
					// PersonByAddressDto
					ModelMapper modelMapper = new ModelMapper();
					personByAddressDto = modelMapper.map(person, PersonStationsDto.class);
					personByAddressDto.setAge(calculateAge(medicalRecord.getBirthdate()));
					medications = medicalRecord.getMedications();
					allergies = medicalRecord.getAllergies();
					personByAddressDto.setMedications(medications);
					personByAddressDto.setAllergies(allergies);

					// on ajoute cette personne à la liste des personnes
					personsStationsDto.add(personByAddressDto);
				}
			}
		}
		// et la liste pour stocker les groupes
		List<StationsPeopleByAddressDto> stationsPeopleByAddress = new ArrayList<>();
		List<StationsDto> stationsDto = new ArrayList<>();
		StationsDto stationDto = new StationsDto();

		for (String adresse : adresses) {
			List<PersonStationsDto> peopleStationsDto = new ArrayList<>();
			for (PersonStationsDto person : personsStationsDto) {
				if (person.getAddress().equalsIgnoreCase(adresse)) {
					peopleStationsDto.add(person);
				}
			}
			// On prépare l'entité groupe par adresse PeopleByAddressDto
			StationsPeopleByAddressDto stationPeopleByAddress = new StationsPeopleByAddressDto();
			stationPeopleByAddress.setAddress(adresse);
			stationPeopleByAddress.setPersonsStationsDto(peopleStationsDto);
			stationsPeopleByAddress.add(stationPeopleByAddress);

			stationDto.setStationsPeopleByAddressDto(stationsPeopleByAddress);
		}
		stationsDto.add(stationDto);

		if (persons.size() == 0) {
			stationsDto = null;
		}
		return stationsDto;
	}

	@Override
	public PersonInfoDto getPersonInfo(String firstName, String lastName) {
		Person person = personsInfosDao.findPersonByFistNameAndLastName(firstName, lastName);
		log.debug("person : " + person);
		if (person != null) {
			MedicalRecord medicalRecord = personsInfosDao.findMedicalRecordsByPerson(person);
			log.debug("dossier med : " + medicalRecord);

			PersonInfoDto personInfoDto = new PersonInfoDto();
			int old = calculateAge(medicalRecord.getBirthdate());
			log.debug("old : " + old);

			ModelMapper modelMapper = new ModelMapper();
			personInfoDto = modelMapper.map(person, PersonInfoDto.class);
			
			personInfoDto.setOld(old);
			personInfoDto.setMedications(medicalRecord.getMedications());
			personInfoDto.setAllergies(medicalRecord.getAllergies());
			return personInfoDto;
		}
		return null;
	}

	@Override
	public CommunityEmailDto getCommunityEmail(String city) {
		CommunityEmailDto emails = new CommunityEmailDto();
		List<String> emailList = personsInfosDao.findEmailByCity(city);
		emails.setEmails(emailList);
		if (emailList.size() == 0) {
			emails = null;
		}
		return emails;
	}

	public static int calculateNumberOfChildren(List<MedicalRecord> listeMedicalRecord) {
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

	public static int calculateNumberOfAdults(List<MedicalRecord> listeMedicalRecord) {
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

	public static int calculateAge(String birthdate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate date = LocalDate.parse(birthdate, formatter);
		LocalDate birthDate = date;
		LocalDate currentDate = LocalDate.now();
		return Period.between(birthDate, currentDate).getYears();
	}
}
