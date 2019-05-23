package io.pismo.payments.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class InvalidOperationTypeException extends RuntimeException {

    public InvalidOperationTypeException() {
        super("Operation type not supported");
    }
}
