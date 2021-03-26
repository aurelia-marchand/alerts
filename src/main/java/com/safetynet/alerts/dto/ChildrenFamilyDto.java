package com.safetynet.alerts.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ChildrenFamilyDto {

	private String firstName;
	private String lastName;
	private int age;
}
