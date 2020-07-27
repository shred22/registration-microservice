package com.registration.request.factories;

import org.openapitools.model.Address;
import org.openapitools.model.RegistrationRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RegistrationApiRequestFactory {

    public RegistrationRequest createRegistrationRequest() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setAge(25);
        registrationRequest.setName("Nautiyal");
        registrationRequest.setAddress(new Address().addressLine1("Tilak nagar").addressLine2("extension").landmark("near lokhandwala").pincode(BigDecimal.TEN));
        registrationRequest.setPhysicalDisability(false);
        registrationRequest.setExistingMember(RegistrationRequest.ExistingMemberEnum.N);
        registrationRequest.setBloodGroup(RegistrationRequest.BloodGroupEnum.A);

        return  registrationRequest;
    }
}
