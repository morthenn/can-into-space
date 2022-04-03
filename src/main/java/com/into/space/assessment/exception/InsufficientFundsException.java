package com.into.space.assessment.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String errorMessage) {
        super(errorMessage);
    }
}
