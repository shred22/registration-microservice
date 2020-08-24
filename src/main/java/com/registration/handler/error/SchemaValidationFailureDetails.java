package com.registration.handler.error;

public class SchemaValidationFailureDetails {
    private String errorMessage;
    private String apiPath;
    private String errorKey;

    public SchemaValidationFailureDetails(String errorMessage, String apiPath, String errorKey) {
        this.errorMessage = errorMessage;
        this.errorKey = errorKey;
        this.apiPath = apiPath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getApiPath() {
        return apiPath;
    }

    public String getErrorKey() {
        return errorKey;
    }

    @Override
    public String toString() {
        return "SchemaValidationFailureDetails{" +
                "errorMessage='" + errorMessage + '\'' +
                ", apiPath='" + apiPath + '\'' +
                ", errorKey='" + errorKey + '\'' +
                '}';
    }
}
