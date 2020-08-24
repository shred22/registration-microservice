package com.registration.service;

import com.registration.repository.AddressRepository;
import com.registration.repository.domain.Address;
import org.openapitools.model.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AddressService.class);

    @CachePut(value = "addressCache", key = "#result.addressId")
    public Address createAddressFromRequest(RegistrationRequest request) {

        return addressRepository.save(Address.builder().addressLine1(request.getAddress().getAddressLine1())
                .addressLine2(request.getAddress().getAddressLine2())
                .landmark(request.getAddress().getLandmark())
                .pincode(Long.valueOf(request.getAddress().getPincode().toString())).build());

    }

    @Cacheable(value = "addressCache", key = "#addressId")
    public Optional<Address> findAddressById(Long addressId) {
        LOG.info("Finding Address with Id {}", addressId);
        return addressRepository.findById(addressId);
    }
}
