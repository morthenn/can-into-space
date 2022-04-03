package com.into.space.assessment.exception;

public class AccountNotExistException extends RuntimeException {

    public AccountNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
