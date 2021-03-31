package com.safetynet.alerts.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class StreetPersonDto {

	String lastName;
	String phone;
	int age;
	List<String> medications;
	List<String> allergies;
}
