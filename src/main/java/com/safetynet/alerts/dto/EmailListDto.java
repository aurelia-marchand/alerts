package com.safetynet.alerts.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class EmailListDto {

	List<String> emails;
}
