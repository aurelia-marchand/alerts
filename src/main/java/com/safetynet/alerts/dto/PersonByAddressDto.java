package com.safetynet.alerts.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Component
public class PersonByAddressDto {
	
	@JsonIgnore
	String firstName;
	String lastName;
	String phone;
	@JsonIgnore
	String address;
	int age;
	List<String> medications;
	List<String> allergies;
}
