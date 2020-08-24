package com.registration.handler.error;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import static java.util.Arrays.asList;

public class SchemaValidationFailedException extends RuntimeException {

    private List<SchemaValidationFailureDetails> failureDetails;

    public SchemaValidationFailedException(JsonProcessingException e) {
        super(e);
    }

    public SchemaValidationFailedException(String message) {
        super(message);
        this.failureDetails = asList(new SchemaValidationFailureDetails(message, null, null));
    }

    public SchemaValidationFailedException(List<SchemaValidationFailureDetails> failureDetailsList) {
        this.failureDetails = failureDetailsList;
    }

    public List<SchemaValidationFailureDetails> getFailureDetails() {
        return failureDetails;
    }
}
