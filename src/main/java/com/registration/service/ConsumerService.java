package com.registration.service;

import com.registration.repository.ConsumerRepository;
import com.registration.repository.domain.Address;
import com.registration.repository.domain.Consumer;
import org.openapitools.model.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class ConsumerService {

    @Autowired
    private ConsumerRepository consumerRepository;
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerService.class);

    @CachePut(value = "consumerCache", key = "#result.consumerId")
    public Consumer createConsumerFromRequest(RegistrationRequest request, Address address) {
        return consumerRepository.save(Consumer.builder()
                .age(request.getAge())
                .name(request.getName())
                .physicalDisability(request.getPhysicalDisability())
                .addressId(address.getAddressId())
                .build());
    }

    @Cacheable(value = "consumerCache", key = "#consumerId")
    public Optional<Consumer> findConsumerById(Long consumerId) {
        LOG.info("Finding Consumer with Id {}", consumerId);
        return consumerRepository.findById(consumerId);
    }

}
