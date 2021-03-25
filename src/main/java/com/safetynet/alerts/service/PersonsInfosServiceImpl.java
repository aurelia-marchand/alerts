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
import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.ChildrenByAddressDto;
import com.safetynet.alerts.dto.EmailListDto;
import com.safetynet.alerts.dto.FamilyMembersDto;
import com.safetynet.alerts.dto.FireListDto;
import com.safetynet.alerts.dto.FloodStationsDto;
import com.safetynet.alerts.dto.PeopleByAddressDto;
import com.safetynet.alerts.dto.PeopleCoveredDto;
import com.safetynet.alerts.dto.PeopleFireDto;
import com.safetynet.alerts.dto.PersonByAddressDto;
import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.dto.PersonsByStationDto;
import com.safetynet.alerts.dto.PhoneAlertDto;
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
	@Autowired
	PeopleCoveredDto peopleCoveredDto;
	@Autowired
	PersonsByStationDto personsByStationDto;

	@Override
	public PeopleCoveredDto getListPersonsByStationNumber(int station) {

		// On récupère les personnes couvertes par la station via les méthodes de
		// l'interface Dao

		PersonsInfos personsInfos = personsInfosDao.findPersonsByStationNumber(station);

		List<Firestation> firestations = personsInfos.getFirestations();
		List<Person> persons = personsInfos.getPersons();
		log.debug("liste 1 avant boucle : size " + persons.size() + "personne" + persons);
		List<PersonsByStationDto> liste = new ArrayList<>();
		// Boucle pour récupérer l'adress de la station puis comparer avec adresse des
		// personnes qu'on récupère si identique
		for (Firestation firestation : firestations) {
			if (firestation.getStation() == station) {
				String address = firestation.getAddress();
				for (Person person : persons) {
					if (person.getAddress().equalsIgnoreCase(address)) {
						// Utilisation ModelMapper pour map Dto/entité
						ModelMapper modelMapper = new ModelMapper();
						personsByStationDto = modelMapper.map(person, PersonsByStationDto.class);
						liste.add(personsByStationDto);
					}
				}
			}
		}
		log.debug("liste 2 après boucle ; size " + persons.size() + "personne :" + liste);
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByPersons(liste);

		// On initialise une liste pour pouvoir y stocker nos résultats
		List<MedicalRecord> listeMedicalRecord = new ArrayList<>();

		// Boucle pour récupérer les données médicales des personnes couvertes par la
		// station
		try {
			for (MedicalRecord medicalRecord : medicalRecords) {
				for (PersonsByStationDto personByStationDto : liste) {
					if (personByStationDto.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
							&& personByStationDto.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {

						listeMedicalRecord.add(medicalRecord);
					}
				}
			}
			log.debug("Récupération des données médicales pour calcul de l'âge");
		} catch (Exception e) {
			log.error("erreur lors de la récupération des informations médicales");
			e.printStackTrace();
		}
		// On Complète notre Dto avant de le renvoyer
		peopleCoveredDto.setNumberOfAdults(calculateNumberOfAdults(listeMedicalRecord));
		peopleCoveredDto.setNumberOfChildren(calculateNumberOfChildren(listeMedicalRecord));
		peopleCoveredDto.setPersonsByStationDto(liste);

		return peopleCoveredDto;
	}

	public static int calculateNumberOfChildren(List<MedicalRecord> listeMedicalRecord) {
		Integer numberOfChildren = 0;
		List<MedicalRecord> listeMedicalRecords = listeMedicalRecord;
		try {
			for (MedicalRecord medicalRecord : listeMedicalRecords) {
				int age = calculateAge(medicalRecord.getBirthdate());
				if (age <= 18) {
					numberOfChildren++;
				}
			}
			log.debug("calcul du nombre d'enfants en cours...");
		} catch (Exception e) {
			log.error("erreur lors du calcul du nombre d'enfants");
			e.printStackTrace();
		}
		return numberOfChildren;
	}

	public static int calculateNumberOfAdults(List<MedicalRecord> listeMedicalRecord) {
		Integer numberOfAdults = 0;
		List<MedicalRecord> listeMedicalRecords = listeMedicalRecord;
		try {
			for (MedicalRecord medicalRecord : listeMedicalRecords) {
				int age = calculateAge(medicalRecord.getBirthdate());
				if (age > 18) {
					numberOfAdults++;
				}
			}
			log.debug("calcul du nombre d'adultes en cours...");
		} catch (Exception e) {
			log.error("erreur lors du calcul du nombre d'adultes");
			e.printStackTrace();
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

	@Override
	public ChildAlertDto getListChildrenByAddress(String address) {
		List<Person> personneByAddress = personsInfosDao.findPersonsByAddress(address);

		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByPerson(personneByAddress);

		List<FamilyMembersDto> familyMembersDto = new ArrayList<>();
		FamilyMembersDto FamilyMember = new FamilyMembersDto();
		ChildrenByAddressDto childrenByAddressDto = new ChildrenByAddressDto();
		List<ChildrenByAddressDto> childrenByAddress = new ArrayList<>();
		ChildAlertDto childAlert = new ChildAlertDto();

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
						FamilyMember = modelMapper.map(person, FamilyMembersDto.class);
						FamilyMember.setAge(age);
						familyMembersDto.add(FamilyMember);
					}
				}
			}
			childAlert.setChildrenByAdress(childrenByAddress);
			childAlert.setFamilyMembersDto(familyMembersDto);

		}

		return childAlert;
	}

	@Override
	public PhoneAlertDto getListPhoneByStation(int station) {

		PersonsInfos personsInfos = personsInfosDao.findPersonsByStationNumber(station);
		List<Firestation> firestations = personsInfos.getFirestations();
		List<Person> persons = personsInfos.getPersons();

		List<String> phones = new ArrayList<>();
		PhoneAlertDto phoneAlertDto = new PhoneAlertDto();

		for (Firestation firestation : firestations) {
			if (firestation.getStation() == station) {
				String address = firestation.getAddress();
				for (Person person : persons) {
					if (person.getAddress().equalsIgnoreCase(address)) {
						String phone = person.getPhone();
						phones.add(phone);
					}
				}
			}
		}

		phoneAlertDto.setPhones(phones);
		log.debug("phoneAlertDto" + phoneAlertDto);
		return phoneAlertDto;
	}

	@Override
	public FireListDto getPeopleByAddress(String address) {
		List<Person> peopleByAddress = personsInfosDao.findPersonsByAddress(address);
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByPerson(peopleByAddress);
		int station = personsInfosDao.getStationByAddress(address);

		List<String> medications = new ArrayList<>();
		List<String> allergies = new ArrayList<>();
		PeopleFireDto peopleFireDto = new PeopleFireDto();

		List<PeopleFireDto> peoplesFireDto = new ArrayList<>();
		FireListDto fireListDto = new FireListDto();

		for (Person personByAddress : peopleByAddress) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (personByAddress.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
						&& personByAddress.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
					medications = medicalRecord.getMedications();
					allergies = medicalRecord.getAllergies();
					log.debug("allergies : " + allergies);

					ModelMapper modelMapper = new ModelMapper();
					peopleFireDto = modelMapper.map(personByAddress, PeopleFireDto.class);
					peopleFireDto.setAge(calculateAge(medicalRecord.getBirthdate()));
					peopleFireDto.setMedications(medications);
					peopleFireDto.setAllergies(allergies);

					peoplesFireDto.add(peopleFireDto);

				}
			}
		}

		fireListDto.setPeopleFireDto(peoplesFireDto);
		fireListDto.setStation(station);

		return fireListDto;
	}

	@Override
	public List<FloodStationsDto> getPeopleByListStation(List<Integer> stationsList) {

		List<Integer> stations = stationsList;

		// On récupère les personnes des stations
		List<Person> persons = personsInfosDao.findPersonsByStation(stations);

		// On récupère les dossiers médicales des personnes concernées
		List<MedicalRecord> medicalRecords = personsInfosDao.findMedicalRecordsByPerson(persons);

		// On prépare deux listes pour stocker les antécédents médicaux (medicaments,
		// posologie et allergies)
		List<String> medications = new ArrayList<>();
		List<String> allergies = new ArrayList<>();

		// On crée une entité personne
		PersonByAddressDto personByAddressDto = new PersonByAddressDto();
		// On prépare la liste pour stocker les personnes à afficher
		List<PersonByAddressDto> peopleByAddress = new ArrayList<>();

		// On prepare un hashset pour stocker les adresses sans doublons.
		Set<String> adresses = new HashSet<>();

		// Boucle pour faire correspondre les personnes et leur dossier médical
		for (Person person : persons) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
						&& person.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {

					adresses.add(person.getAddress());
					// on intègre la personne, ses données médicales et son âge dans l'entité
					// PersonByAddressDto
					ModelMapper modelMapper = new ModelMapper();
					personByAddressDto = modelMapper.map(person, PersonByAddressDto.class);
					personByAddressDto.setAge(calculateAge(medicalRecord.getBirthdate()));
					medications = medicalRecord.getMedications();
					allergies = medicalRecord.getAllergies();
					personByAddressDto.setMedications(medications);
					personByAddressDto.setAllergies(allergies);
					// log.debug("personByAddressDto : " + personByAddressDto);

					// on ajoute cette personne à la liste des personnes
					peopleByAddress.add(personByAddressDto);

				}
			}

		}

		// et la liste pour stocker les groupes
		List<PeopleByAddressDto> peoplesByAddress = new ArrayList<>();

		List<FloodStationsDto> floods = new ArrayList<>();

		FloodStationsDto floodStationDto = new FloodStationsDto();

		for (String adresse : adresses) {
			List<PersonByAddressDto> people = new ArrayList<>();

			for (PersonByAddressDto person : peopleByAddress) {
				if (person.getAddress().equalsIgnoreCase(adresse)) {
					people.add(person);
				}
			}
			// On prépare l'entité groupe par adresse PeopleByAddressDto
			PeopleByAddressDto peopleByAddressDto = new PeopleByAddressDto();
			peopleByAddressDto.setAddress(adresse);
			peopleByAddressDto.setPersonsByAddressDto(people);
			peoplesByAddress.add(peopleByAddressDto);

			floodStationDto.setPeoplesByAddressDto(peoplesByAddress);
		}
		floods.add(floodStationDto);
		return floods;
	}

	@Override
	public PersonInfoDto getPersonInfo(String firstName, String lastName) {
		Person person = personsInfosDao.getPerson(firstName, lastName);
		log.debug("person : " + person);
		MedicalRecord medicalRecord = personsInfosDao.findMedicalRecordsByPerson(person);
		log.debug("dossier med : " + medicalRecord);
		PersonInfoDto personInfoDto = new PersonInfoDto();
		int old = calculateAge(medicalRecord.getBirthdate());
		log.debug("old : " + old);

		ModelMapper modelMapper = new ModelMapper();
		personInfoDto = modelMapper.map(person, PersonInfoDto.class);
		//
		personInfoDto.setOld(old);
		personInfoDto.setMedications(medicalRecord.getMedications());
		personInfoDto.setAllergies(medicalRecord.getAllergies());
		
		return personInfoDto;
	}

	@Override
	public EmailListDto getCommunityEmail(String city) {
		EmailListDto emails = new EmailListDto();
		List<String> emailList = personsInfosDao.findEmailByCity(city);
		emails.setEmails(emailList);
		return emails;
	}
}
