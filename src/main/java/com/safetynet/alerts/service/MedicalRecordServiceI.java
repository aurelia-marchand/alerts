package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.MedicalRecord;

public interface MedicalRecordServiceI {

	List<MedicalRecord> getListMedicalRecords();

	MedicalRecord getMedicalRecord(String firstName, String lastName);

	MedicalRecord deleteMedicalRecord(String firstName, String lastName);

	MedicalRecord putMedicalRecord(MedicalRecord medicalRecordToPut);

	MedicalRecord postMedicalRecord(MedicalRecord medicalRecordToPost);

}
