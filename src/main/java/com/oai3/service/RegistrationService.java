package com.oai3.service;

import com.oai3.repository.AddressRepository;
import com.oai3.repository.ConsumerRepository;
import com.oai3.repository.RegistrationRepository;
import com.oai3.repository.domain.Address;
import com.oai3.repository.domain.Consumer;
import com.oai3.repository.domain.Registrations;
import org.openapitools.model.CandidateDetails;
import org.openapitools.model.RegistrationDetailResponse;
import org.openapitools.model.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ConsumerRepository consumerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationService.class);


    @CachePut(value = "registrationsCache", key = "#result.registrationId")
    public Registrations saveRegistrations(RegistrationRequest request) {

        Address address = addressRepository.save(createAddressFromRequest(request));
        Consumer consumer = createConsumerFromRequest(request);
        consumer.setAddressId(address.getAddressId());
        consumer = consumerRepository.save(consumer);
        LOG.info("Saving Registration with address id {} and consumer Id {}", address.getAddressId(), consumer.getConsumerId());
        return registrationRepository.save(createRegistrations(consumer.getConsumerId()));
    }

    @Cacheable(value = "registrationsCache", key = "#result.addressId")
    private Address createAddressFromRequest(RegistrationRequest request) {
        return Address.builder().addressLine1(request.getAddress().getAddressLine1())
                .addressLine2(request.getAddress().getAddressLine2())
                .landmark(request.getAddress().getLandmark())
                .pincode(Long.valueOf(request.getAddress().getPincode().toString())).build();
    }

    @Cacheable(value = "registrationsCache", key = "#result.consumerId")
    private Consumer createConsumerFromRequest(RegistrationRequest request) {
        return Consumer.builder()
                .age(request.getAge())
                .name(request.getName())
                .physicalDisability(request.getPhysicalDisability())
                .build();

    }

    private Registrations createRegistrations(Long consumerId) {
        return Registrations.builder().consumerId(consumerId).status(ACTIVE.getValue()).build();
    }


    public RegistrationDetailResponse getRegistrationDetails(Long regId) {
        Registrations registrations = findRegistrationById(regId).orElseThrow(EntityNotFoundException::new);
        Consumer consumer = consumerRepository.findById(registrations.getConsumerId()).orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findById(consumer.getAddressId()).orElseThrow(EntityNotFoundException::new);
        return buildRegistrationDetailResponse(registrations, consumer, address);
    }

    private RegistrationDetailResponse buildRegistrationDetailResponse(Registrations registrations, Consumer consumer, Address address) {
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

    @Cacheable(value = "registrationsCache", key = "#consumerId")
    public Optional<Consumer> findConsumerById(Long consumerId) {
        LOG.info("Finding Consumer with Id {}", consumerId);
        return consumerRepository.findById(consumerId);
    }

    @Cacheable(value = "registrationsCache", key = "#addressId")
    public Optional<Address> findAddressById(Long addressId) {
        LOG.info("Finding Address with Id {}", addressId);
        return addressRepository.findById(addressId);
    }
}
