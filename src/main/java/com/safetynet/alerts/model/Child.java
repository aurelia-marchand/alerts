package com.safetynet.alerts.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Component
@EqualsAndHashCode(callSuper=true)
public class Child extends Person {

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
