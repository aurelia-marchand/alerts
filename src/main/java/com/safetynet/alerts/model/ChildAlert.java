package com.safetynet.alerts.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ChildAlert {

	List<Child> children;
	
	List<Adult> adults;
	
}
