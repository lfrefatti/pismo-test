package io.pismo.payments.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(){
        super("Your credit card limit was reached");
    }

}
