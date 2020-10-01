package com.registration.integration.test;


import org.openapitools.model.AuthenticationRequest;
import org.openapitools.model.AuthenticationResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public abstract class BaseIntegrationTest {

    public static void main(String[] args) {
        testIt();
    }

    protected String getAuthToken() {
        RestTemplate restTemplate = new RestTemplate();
        TestRestTemplate testRestTemplate =  new TestRestTemplate();

        String uri = "http://localhost:7887/authenticate";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("aka");
        request.setPassword("password");

        HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, AuthenticationResponse.class);
        return responseEntity.getBody().getJwt();
    }

    public static void testIt() {
        List<String> strings = null;


        for(String s : strings) {
            System.out.println(" : : "+s);
        }

    }

}
