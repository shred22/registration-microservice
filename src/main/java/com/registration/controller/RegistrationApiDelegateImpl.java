package com.registration.controller;

import com.registration.repository.domain.Address;
import com.registration.repository.domain.Consumer;
import com.registration.repository.domain.Registrations;
import com.registration.service.AddressService;
import com.registration.service.ConsumerService;
import com.registration.service.RegistrationService;
import org.openapitools.api.RegisterApiDelegate;
import org.openapitools.model.RegistrationDetailResponse;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static org.openapitools.model.RegistrationResponse.StatusEnum.valueOf;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;


@Component
public class RegistrationApiDelegateImpl implements RegisterApiDelegate {


    private static final Logger LOG = LoggerFactory.getLogger(RegistrationApiDelegateImpl.class);
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ConsumerService consumerService;


    @Override
    @Transactional
    public ResponseEntity<RegistrationResponse> registerPost(String authToken, RegistrationRequest registrationRequest) {
        LOG.info("Request Landed with Header Param : {} and Request Body as : {}", authToken, registrationRequest);
        Registrations registrations = registrationService.saveRegistrations(registrationRequest);;
        LOG.info("Returning response with Registration Id and other details ");
        return status(CREATED).body(new RegistrationResponse().registrationId(registrations.getRegistrationId()).status(valueOf(registrations.getStatus())));
    }

    @Override
    public ResponseEntity<RegistrationDetailResponse> registerRegIdGet(Long regId) {
        Registrations registrations = registrationService.findRegistrationById(regId).orElseThrow(EntityNotFoundException::new);
        Consumer consumer = consumerService.findConsumerById(registrations.getConsumerId()).orElseThrow(EntityNotFoundException::new);
        Address address = addressService.findAddressById(consumer.getAddressId()).orElseThrow(EntityNotFoundException::new);
        return ok(registrationService.buildRegistrationDetailResponse(registrations, consumer, address));
    }

}
