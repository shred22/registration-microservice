package com.oai3.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegistrationApiTest {

    @LocalServerPort
    private int randomServerPort;
    @Value("${api.url}")
    private String url;

    @Test
    public void testAPI() {

        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .uriVariables(Map.of("regId",123));

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", "auth-token-1234");
        RegistrationRequest request = new RegistrationRequest();
        request.setName("shreyas");
        request.setAge("19");

        HttpEntity<RegistrationRequest> entity = new HttpEntity<>(request, headers); //Update this as per your code

        ResponseEntity<RegistrationResponse> response = restTemplate.exchange(builder.build().encode().toUri(),
                HttpMethod.POST, entity, RegistrationResponse.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(HttpStatus.CREATED.equals(response.getStatusCode()));
        Assert.assertNotNull(response.getBody().getStatus());
        Assert.assertTrue(RegistrationResponse.StatusEnum.APPROVED.equals(response.getBody().getStatus()));
    }

}