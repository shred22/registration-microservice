package com.oai3.controller;

import org.openapitools.api.RegisterApiDelegate;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RegistrationApiDelegateImpl implements RegisterApiDelegate {


    private static final Logger LOG = LoggerFactory.getLogger(RegistrationApiDelegateImpl.class);

    @Override
    public ResponseEntity<RegistrationResponse>  registerRegIdPost(String authToken,
                                                              String regId,
                                                              RegistrationRequest registrationRequest) {

        LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>   Header Parameter is : " + authToken);
        LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>   Path Parameter is : " + regId);
        LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>   Request is : " + registrationRequest);
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.status(RegistrationResponse.StatusEnum.APPROVED);
        return ResponseEntity.status(HttpStatus.CREATED).header(" X-RateLimit-Limit", "10").body(registrationResponse);

    }

}
