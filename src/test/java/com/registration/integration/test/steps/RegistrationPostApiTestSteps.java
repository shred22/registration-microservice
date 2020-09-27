package com.registration.integration.test.steps;

import com.registration.integration.test.BaseIntegrationTest;
import com.registration.request.factories.RegistrationApiRequestFactory;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class RegistrationPostApiTestSteps extends BaseIntegrationTest  {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationPostApiTestSteps.class);
    protected RegistrationRequest registrationRequest;
    @Value("${registration.api.url}")
    protected String endpoint;
    @Autowired
    protected RegistrationApiRequestFactory requestFactory = new RegistrationApiRequestFactory();
    protected RegistrationResponse registrationResponse;
    protected HttpStatus statusCode;
    protected HttpHeaders responseHeaders;
    protected String jwt;
    protected Long registrationId;


    public RegistrationPostApiTestSteps() {



    }

}
