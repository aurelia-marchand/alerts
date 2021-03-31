package com.safetynet.alerts.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Component
public class Adult extends Person{
	
	private String firstName;

	private String lastName;
	
	private int age;
	
	@JsonIgnore
	private String address;
	@JsonIgnore
	private String city;
	@JsonIgnore
	private int zip;
	@JsonIgnore
	private String phone;
	@JsonIgnore
	private String email;
	
	
}
