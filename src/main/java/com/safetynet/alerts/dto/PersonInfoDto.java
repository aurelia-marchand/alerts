package com.safetynet.alerts.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class PersonInfoDto {

	String lastName;
	String address;
	int old;
	String email;
	List<String> medications;
	List<String> allergies;
}
