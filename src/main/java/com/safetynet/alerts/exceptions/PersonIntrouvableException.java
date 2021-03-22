package com.safetynet.alerts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonIntrouvableException extends RuntimeException {

	public PersonIntrouvableException(String s) {
        super(s);
    }
	
}
