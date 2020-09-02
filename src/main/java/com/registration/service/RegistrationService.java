package com.registration.service;

import com.registration.repository.RegistrationRepository;
import com.registration.repository.domain.Address;
import com.registration.repository.domain.Consumer;
import com.registration.repository.domain.Registrations;
import org.openapitools.model.CandidateDetails;
import org.openapitools.model.RegistrationDetailResponse;
import org.openapitools.model.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.openapitools.model.RegistrationDetailResponse.StatusEnum.ACTIVE;
import static org.openapitools.model.RegistrationDetailResponse.StatusEnum.valueOf;

@Service
public class RegistrationService {

    @Autowired
    private AddressService addressService;
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private RegistrationRepository registrationRepository;

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationService.class);


    @CachePut(value = "registrationsCache", key = "#result.registrationId")
    public Registrations saveRegistrations(RegistrationRequest request) {

        Address address = addressService.createAddressFromRequest(request);
        Consumer consumer = consumerService.createConsumerFromRequest(request, address);
        LOG.info("Saving Registration with address id {} and consumer Id {}", address.getAddressId(), consumer.getConsumerId());
        return registrationRepository.save(createRegistrations(consumer.getConsumerId()));
    }


    private Registrations createRegistrations(Long consumerId) {
        return Registrations.builder().consumerId(consumerId).status(ACTIVE.getValue()).build();
    }

    public RegistrationDetailResponse getRegistrationDetails(Long regId) {
        Registrations registrations = findRegistrationById(regId).orElseThrow(EntityNotFoundException::new);
        Consumer consumer = consumerService.findConsumerById(registrations.getConsumerId()).orElseThrow(EntityNotFoundException::new);
        Address address = addressService.findAddressById(consumer.getAddressId()).orElseThrow(EntityNotFoundException::new);
        return buildRegistrationDetailResponse(registrations, consumer, address);
    }


    public RegistrationDetailResponse buildRegistrationDetailResponse(Registrations registrations, Consumer consumer, Address address) {
        LOG.info("Building Registration Details Response");
        org.openapitools.model.Address responseAddress = new org.openapitools.model.Address().addressLine1(address.getAddressLine1()).addressLine2(address.getAddressLine2()).pincode(BigDecimal.valueOf(address.getPincode())).landmark(address.getLandmark());
        CandidateDetails candidateDetails = new CandidateDetails().age(consumer.getAge()).name(consumer.getName()).address(responseAddress);
        return  new RegistrationDetailResponse().registrationId(registrations.getRegistrationId()).candidateDetails(candidateDetails).status(valueOf(registrations.getStatus()));
    }


    @Cacheable(value = "registrationsCache", key = "#regId")
    public Optional<Registrations> findRegistrationById(Long regId) {
        LOG.info("Finding Registration with Id {}", regId);
        return registrationRepository.findById(regId);
    }

    @CacheEvict(value = "registrationsCache", key = "#regId")
    public int deleteRegistrationById(Long regId) {
        LOG.info("Deleting Registration with Id {}", regId);
        return registrationRepository.softDeleteRegistrationById(regId);
    }

}
