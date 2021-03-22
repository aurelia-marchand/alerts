package com.safetynet.alerts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PersonDejaExistanteException extends RuntimeException {

	public PersonDejaExistanteException(String s) {
        super(s);
    }
}
