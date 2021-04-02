package com.safetynet.alerts.model;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MedicalRecord {

	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	private String birthdate;
	
	private List<String> medications;
	
	private List<String> allergies;

	public MedicalRecord(String firstName, String lastName, String birthdate, List<String> medications,
			List<String> allergies) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.medications = medications;
		this.allergies = allergies;
	}

	public MedicalRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

}
