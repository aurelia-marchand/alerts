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
import com.safetynet.alerts.dto.DistrictPersonDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.PersonStationsDto;
import com.safetynet.alerts.dto.StationsPeopleByAddressDto;
import com.safetynet.alerts.dto.StreetPersonDto;
import com.safetynet.alerts.model.Adult;
import com.safetynet.alerts.model.PhoneAlert;
import com.safetynet.alerts.model.Child;
import com.safetynet.alerts.model.ChildAlert;
import com.safetynet.alerts.model.CommunityEmail;
import com.safetynet.alerts.model.DistrictPeople;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.StationsPeople;
import com.safetynet.alerts.model.StreetPeople;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class PersonsInfosServiceImpl implements PersonsInfosServiceI {

	@Autowired
	PersonsInfosDaoI personsInfosDao;

	@Override
	public DistrictPeople getListPersonsByStationNumber(int station) {

		// Récupération des personnes couvertes par la station
		List<Person> persons = personsInfosDao.findPersonsByStationNumber(station);

		// Préparation de la liste et des entités pour stocker les personnes avec
		// uniquement les
		// valeurs qu'on veut renvoyer
		List<DistrictPersonDto> districtPeopleListDto = new ArrayList<>();
		DistrictPeople districtPeople = new DistrictPeople();
		DistrictPersonDto districtPersonsDto = new DistrictPersonDto();

		for (Person person : persons) {
			ModelMapper modelMapper = new ModelMapper();
			districtPersonsDto = modelMapper.map(person, DistrictPersonDto.class);
			districtPeopleListDto.add(districtPersonsDto);
		}

		// Récupération des données médicales des personnes sur notre liste persons
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByListPerson(persons);

		// On Complète notre Dto avant de le renvoyer
		districtPeople.setNumberOfAdults(calculateNumberOfAdults(medicalRecords));
		log.debug("Calcul du nombre d'adultes : " + districtPeople.getNumberOfAdults());
		districtPeople.setNumberOfChildren(calculateNumberOfChildren(medicalRecords));
		log.debug("Calcul du nombre d'enfants : " + districtPeople.getNumberOfChildren());
		districtPeople.setDistrictPersonsDto(districtPeopleListDto);

		return districtPeople;
	}

	@Override
	public ChildAlert getListChildrenByAddress(String address) {
		// Récupération données personnes et médicales
		List<Person> personneByAddress = personsInfosDao.findPersonsByAddress(address);
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByListPerson(personneByAddress);

		// Préparation des entités de stockage à renvoyer
		Child child = new Child();
		Adult adult = new Adult();
		List<Child> children = new ArrayList<>();
		List<Adult> adults = new ArrayList<>();

		ChildAlert childAlert = new ChildAlert();

		// Boucle pour lier personnes et leurs dossiers medicaux, calculer leurs âges et
		// les
		// placer soit comme enfant soit comme membres de la famille
		for (Person person : personneByAddress) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())
						&& medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())) {
					int age = calculateAge(medicalRecord.getBirthdate());
					if (age <= 18) {

						ModelMapper modelMapper = new ModelMapper();
						child = modelMapper.map(person, Child.class);
						child.setAge(age);
						children.add(child);
					} else {
						ModelMapper modelMapper = new ModelMapper();
						adult = modelMapper.map(person, Adult.class);
						adult.setAge(age);
						adults.add(adult);
					}
				}
			}
			childAlert.setChildren(children);
			childAlert.setAdults(adults);
		}

		if (childAlert.getChildren() == null && childAlert.getAdults() == null) {
			childAlert = null;
		}
		return childAlert;
	}

	@Override
	public PhoneAlert getListPhoneByStation(int station) {
		// Récupération des numéros de téléphones par station
		List<String> phones = personsInfosDao.findPhoneByStationNumber(station);

		// Préparation entité de stockage et tranfert
		PhoneAlert phoneAlert = new PhoneAlert();

		phoneAlert.setPhones(phones);
		log.debug("phoneAlertDto" + phoneAlert);

		if (phones.size() == 0) {
			phoneAlert = null;
		}

		return phoneAlert;
	}

	@Override
	public StreetPeople getPeopleByAddress(String address) {
		// Récupération des personnes à l'adresse et de leurs dossiers médicaux
		List<Person> peopleByAddress = personsInfosDao.findPersonsByAddress(address);
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByListPerson(peopleByAddress);
		// Récupération du numéro de la station
		int station = personsInfosDao.findStationByAddress(address);

		// Préparation entités de transfert
		List<String> medications = new ArrayList<>();
		List<String> allergies = new ArrayList<>();
		StreetPersonDto streetPersonDto = new StreetPersonDto();
		List<StreetPersonDto> streetPeopleList = new ArrayList<>();
		StreetPeople streetPeople = new StreetPeople();

		// Boucle association personnes/dossiers médicaux
		for (Person personByAddress : peopleByAddress) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (personByAddress.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
						&& personByAddress.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
					medications = medicalRecord.getMedications();
					allergies = medicalRecord.getAllergies();

					ModelMapper modelMapper = new ModelMapper();
					streetPersonDto = modelMapper.map(personByAddress, StreetPersonDto.class);
					streetPersonDto.setAge(calculateAge(medicalRecord.getBirthdate()));
					streetPersonDto.setMedications(medications);
					streetPersonDto.setAllergies(allergies);

					streetPeopleList.add(streetPersonDto);
				}
			}
		}
		streetPeople.setStreetPeopleDto(streetPeopleList);
		streetPeople.setStation(station);

		if (streetPeople.getStreetPeopleDto() == null && streetPeople.getStation() == 0) {
			streetPeople = null;
		}

		return streetPeople;
	}

	@Override
	public List<StationsPeople> getPeopleByListStation(List<Integer> stationsList) {

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
		PersonStationsDto personStationsDto = new PersonStationsDto();
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
					personStationsDto = modelMapper.map(person, PersonStationsDto.class);
					personStationsDto.setAge(calculateAge(medicalRecord.getBirthdate()));
					medications = medicalRecord.getMedications();
					allergies = medicalRecord.getAllergies();
					personStationsDto.setMedications(medications);
					personStationsDto.setAllergies(allergies);

					// on ajoute cette personne à la liste des personnes
					personsStationsDto.add(personStationsDto);
				}
			}
		}
		// et la liste pour stocker les groupes
		List<StationsPeopleByAddressDto> stationsPeopleByAddress = new ArrayList<>();
		List<StationsPeople> stationsPeopleList = new ArrayList<>();
		StationsPeople stationsPeople = new StationsPeople();

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

			stationsPeople.setStationsPeopleByAddressDto(stationsPeopleByAddress);
		}
		stationsPeopleList.add(stationsPeople);

		if (persons.size() == 0) {
			stationsPeopleList = null;
		}
		return stationsPeopleList;
	}

	@Override
	public List<PersonInfoDto> getPersonInfo(String firstName, String lastName) {
		List<Person> personsList = personsInfosDao.findPersonByFistNameAndLastName(firstName, lastName);
		log.debug("person : " + personsList);
		if (personsList != null) {
			List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByListPerson(personsList);
			log.debug("dossier med : " + medicalRecords);

			List<PersonInfoDto> personInfoDtoList = new ArrayList<>();
			Person person = new Person();
			person.setFirstName(firstName);
			person.setLastName(lastName);

			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
						&& person.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
					PersonInfoDto personInfoDto = new PersonInfoDto();
					int old = calculateAge(medicalRecord.getBirthdate());

					ModelMapper modelMapper = new ModelMapper();
					personInfoDto = modelMapper.map(person, PersonInfoDto.class);
					personInfoDto.setOld(old);
					personInfoDto.setMedications(medicalRecord.getMedications());
					personInfoDto.setAllergies(medicalRecord.getAllergies());
					personInfoDtoList.add(personInfoDto);
					for (Person personne : personsList) {
						if (personne.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
								&& personne.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
							personInfoDto.setEmail(personne.getEmail());
							personInfoDto.setAddress(personne.getAddress());

						}
					}
				}
			}

			return personInfoDtoList;
		}
		return null;
	}

	@Override
	public CommunityEmail getCommunityEmail(String city) {
		CommunityEmail emails = new CommunityEmail();
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
