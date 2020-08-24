package com.registration.handler.error;

public class SecurityFailedException  extends  RuntimeException {

    private String message;

    public SecurityFailedException(String message) {
        super(message);
    }

    public SecurityFailedException(Exception e) {
        super(e);
    }
}
