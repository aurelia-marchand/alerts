package com.safetynet.alerts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DonneeDejaExistanteException extends RuntimeException {

	public DonneeDejaExistanteException(String s) {
        super(s);
    }
}
