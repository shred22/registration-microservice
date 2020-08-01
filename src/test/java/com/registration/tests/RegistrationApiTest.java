package com.registration.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.registration.request.factories.RegistrationApiRequestFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@Import(RegistrationApiRequestFactory.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, value="com.registration")

public class RegistrationApiTest {

    @LocalServerPort
    private int randomServerPort;
    @Value("${registration.api.url}")
    private String url;
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationApiTest.class);
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RegistrationApiRequestFactory requestFactory;

    @Test
    @Rollback
    public void testAPI() throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", "auth-token-1234");
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        RegistrationRequest request = requestFactory.createRegistrationRequest();

        LOG.info("Sending Request >>>> {} to endpoint {} ", mapper.writeValueAsString(request), url);
        HttpEntity<RegistrationRequest> entity = new HttpEntity<>(request, headers); //Update this as per your code

        ResponseEntity<RegistrationResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, RegistrationResponse.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(HttpStatus.CREATED.equals(response.getStatusCode()));
        Assert.assertNotNull(response.getBody().getStatus());
        Assert.assertTrue(RegistrationResponse.StatusEnum.ACTIVE.equals(response.getBody().getStatus()));
    }



}
