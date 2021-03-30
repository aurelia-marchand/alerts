package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dao.MedicalRecordDaoI;
import com.safetynet.alerts.model.MedicalRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicalRecordServiceImpl implements MedicalRecordServiceI {

	@Autowired
	private MedicalRecordDaoI medicalRecordDao;

	@Override
	public List<MedicalRecord> getListMedicalRecords() {
		log.debug("Etape service get list medical records");
		return medicalRecordDao.findAllMedicalRecords();
	}

	@Override
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {
		log.debug("service demande au dao dossier médical de : " + firstName + " " + lastName);
		return medicalRecordDao.findMedicalRecordByFirstNameAndLastName(firstName, lastName);
	}

	@Override
	public MedicalRecord deleteMedicalRecord(String firstName, String lastName) {
		MedicalRecord medicalRecordToDelete = medicalRecordDao.findMedicalRecordByFirstNameAndLastName(firstName, lastName);

		log.debug("envoi du dossier à supprimer au dao");
		medicalRecordDao.deleteMedicalRecordByFirstNameAndLastName(firstName, lastName);

		return medicalRecordToDelete;

	}

	@Override
	public MedicalRecord putMedicalRecord(MedicalRecord medicalRecordToPut) {
		MedicalRecord medicalRecord = medicalRecordDao.findMedicalRecordByFirstNameAndLastName(medicalRecordToPut.getFirstName(), medicalRecordToPut.getLastName());
		
		//Mise à jour du dossier de la personne selon les valeurs reçues
				String birthdate = medicalRecordToPut.getBirthdate();
				if(birthdate != null) {
					log.debug("reçu valeur birthdate :" + birthdate);
					medicalRecord.setBirthdate(birthdate);
				}
				
				List<String> medications = medicalRecordToPut.getMedications();
				if(medications != null) {
					log.debug("reçu valeur medications : " + medications);
					medicalRecord.setMedications(medications);
				}
				
				List<String> allergies = medicalRecordToPut.getAllergies();
				if(allergies != null) {
					log.debug("reçu valeur medications : " + allergies);
					medicalRecord.setAllergies(allergies);
				}
		medicalRecordDao.updateMedicalRecord(medicalRecord);
		return medicalRecordToPut;
	}

	@Override
	public MedicalRecord postMedicalRecord(MedicalRecord medicalRecord) {
		String firstName = medicalRecord.getFirstName();
		String lastName = medicalRecord.getLastName();
		
		MedicalRecord medicalRecordToPost = medicalRecordDao.findMedicalRecordByFirstNameAndLastName(firstName, lastName);
		
		if(medicalRecordToPost == null) {
			log.debug("envoi de la personne à créer au dao");
			medicalRecordDao.saveMedicalRecord(medicalRecord);
		} else {
			log.error("Dossier déjà existant");
		}
		
		return medicalRecord;
	}

}
