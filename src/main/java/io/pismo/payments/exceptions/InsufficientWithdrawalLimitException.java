package io.pismo.payments.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class InsufficientWithdrawalLimitException extends RuntimeException {

    public InsufficientWithdrawalLimitException(){
        super("Your withdrawal limit was reached");
    }

}
