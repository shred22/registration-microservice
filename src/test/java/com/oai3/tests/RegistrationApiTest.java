package com.oai3.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openapitools.model.Address;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

import static org.openapitools.model.RegistrationRequest.BloodGroupEnum.A;
import static org.openapitools.model.RegistrationRequest.ExistingMemberEnum.Y;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegistrationApiTest {

    @LocalServerPort
    private int randomServerPort;
    @Value("${api.url}")
    private String url;
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationApiTest.class);
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testAPI() throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .uriVariables(Map.of("regId",123));

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", "auth-token-1234");
        RegistrationRequest request = createRegistrationRequest();

        LOG.info("Sending Request >>>> {} ", mapper.writeValueAsString(request));
        HttpEntity<RegistrationRequest> entity = new HttpEntity<>(request, headers); //Update this as per your code

        ResponseEntity<RegistrationResponse> response = restTemplate.exchange(builder.build().encode().toUri(),
                HttpMethod.POST, entity, RegistrationResponse.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(HttpStatus.CREATED.equals(response.getStatusCode()));
        Assert.assertNotNull(response.getBody().getStatus());
        Assert.assertTrue(RegistrationResponse.StatusEnum.ACTIVE.equals(response.getBody().getStatus()));
    }

    public RegistrationRequest createRegistrationRequest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("Shreyas");
        request.setAge(25);
        request.setBloodGroup(A);
        request.setExistingMember(Y);
        request.setPhysicalDisability(false);
        Address address = new Address();
        address.setAddressLine1("290 Sarvasampann Nagar");
        address.setAddressLine2("Kanadia Road");
        address.setLandmark("Near Patidar Kirana Store");
        address.setPincode(BigDecimal.valueOf(452016));
        request.setAddress(address);

        return request;

    }

}
