package com.collegemanagement.Exception;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String message) {
        super(message);
    }
}
