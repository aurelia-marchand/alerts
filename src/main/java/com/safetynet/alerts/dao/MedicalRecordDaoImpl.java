package com.safetynet.alerts.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonsInfos;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MedicalRecordDaoImpl implements MedicalRecordDaoI {

	@Autowired
	AccessJsonI accessJson;
	
	@Override
	public List<MedicalRecord> findAllMedicalRecords() {

		List<MedicalRecord> medicalRecords;
		try {
			medicalRecords = accessJson.getData().getMedicalrecords();
			return medicalRecords;
		} catch (Exception e) {
			log.error("erreur pendant la récupération de la liste de médical record : " + e);
		}
		return null;
	}

	@Override
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {
		List<MedicalRecord> medicalRecords = accessJson.getData().getMedicalrecords();
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equalsIgnoreCase(firstName) && medicalRecord.getLastName().equalsIgnoreCase(lastName)) {
				log.debug("dao renvoi : "+ medicalRecord);
				return medicalRecord;
			}
		}
		return null;
	}

	@Override
	public MedicalRecord deleteMedicalRecord(String firstName, String lastName) {
		PersonsInfos personsInfos = accessJson.getData();
		List<MedicalRecord> medicalRecords = findAllMedicalRecords();
		MedicalRecord medicalRecordToDelete = null;
		
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equalsIgnoreCase(firstName) && medicalRecord.getLastName().equalsIgnoreCase(lastName)) {
				medicalRecordToDelete = medicalRecord;
				log.debug("personne a supprimer trouvée :  "+ medicalRecordToDelete.toString());
			}
		}
		medicalRecords.remove(medicalRecordToDelete);

		personsInfos.setMedicalrecords(medicalRecords);

		accessJson.writeData(personsInfos);
		return null;
	}

	@Override
	public MedicalRecord putMedicalRecord(MedicalRecord medicalRecordToPut) {
		PersonsInfos personsInfos = accessJson.getData();
		List<MedicalRecord> medicalRecords = findAllMedicalRecords();
		MedicalRecord medicalRecordToUpdate = medicalRecordToPut;
		int index = 0;
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equalsIgnoreCase(medicalRecordToUpdate.getFirstName()) && medicalRecord.getLastName().equalsIgnoreCase(medicalRecordToUpdate.getLastName())) {
				medicalRecordToUpdate = medicalRecord;
				index = medicalRecords.indexOf(medicalRecord);
				log.debug("index récupéré pour update");
			}
		}
		medicalRecords.set(index, medicalRecordToPut);
	personsInfos.setMedicalrecords(medicalRecords);
	accessJson.writeData(personsInfos);
		return null;
	}

	@Override
	public MedicalRecord postMedicalRecord(MedicalRecord medicalRecordToPost) {
		PersonsInfos personsInfos = accessJson.getData();
		List<MedicalRecord> medicalRecords = findAllMedicalRecords();
		MedicalRecord newMedicalRecord = medicalRecordToPost;
		
		medicalRecords.add(newMedicalRecord);
		personsInfos.setMedicalrecords(medicalRecords);

		accessJson.writeData(personsInfos);

		return null;
	}

}
