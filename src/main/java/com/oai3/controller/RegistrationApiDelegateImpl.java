package com.oai3.controller;

import com.oai3.repository.domain.Registrations;
import com.oai3.service.RegistrationService;
import org.openapitools.api.RegisterApiDelegate;
import org.openapitools.model.RegistrationDetailResponse;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;


@Component
public class RegistrationApiDelegateImpl implements RegisterApiDelegate {


    private static final Logger LOG = LoggerFactory.getLogger(RegistrationApiDelegateImpl.class);
    @Autowired
    private RegistrationService service;


    @Override
    @Transactional
    public ResponseEntity<RegistrationResponse> registerPost(String authToken, RegistrationRequest registrationRequest) {

        LOG.info("Request Landed with Header Param : {} and Request Body as : {}", authToken, registrationRequest);
        Registrations registrations = service.saveRegistrations(registrationRequest);
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setRegistrationId(registrations.getRegistrationId());
        registrationResponse.status(RegistrationResponse.StatusEnum.ACTIVE);
        LOG.info("Returning response with Registration Id {} ", registrationResponse.getRegistrationId());
        return ResponseEntity.status(HttpStatus.CREATED).header(" X-RateLimit-Limit", "10").body(registrationResponse);

    }

    @Override
    public ResponseEntity<RegistrationDetailResponse> registerRegIdGet(Long regId) {
        return ResponseEntity.ok(service.getRegistrationDetails(regId));
    }

}
