package com.safetynet.alerts.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ChildrenByAddressDto {
	
	private String firstName;
	private String lastName;
	private String address;
	private int age;
	
}
