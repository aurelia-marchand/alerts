package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.model.MedicalRecord;

public interface MedicalRecordDaoI {

	List<MedicalRecord> findAllMedicalRecords();

	MedicalRecord findMedicalRecordByFirstNameAndLastName(String firstName, String lastName);

	MedicalRecord deleteMedicalRecordByFirstNameAndLastName(String firstName, String lastName);

	MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);

	MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);

}
