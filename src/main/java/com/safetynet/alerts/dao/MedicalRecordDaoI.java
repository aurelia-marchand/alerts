package com.safetynet.alerts.dao;

import java.util.List;

import com.safetynet.alerts.model.MedicalRecord;

public interface MedicalRecordDaoI {

	List<MedicalRecord> findAllMedicalRecords();

	MedicalRecord getMedicalRecord(String firstName, String lastName);

	MedicalRecord deleteMedicalRecord(String firstName, String lastName);

	MedicalRecord putMedicalRecord(MedicalRecord medicalRecord);

	MedicalRecord postMedicalRecord(MedicalRecord medicalRecord);

}
