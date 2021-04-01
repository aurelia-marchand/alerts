package com.safetynet.alerts.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Firestation {

	@NotBlank
	private String address;
	
	@NotNull
	@Min(value = 1)
	private int station;
	
}
