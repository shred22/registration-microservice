package com.registration.integration.test;


import org.junit.runner.RunWith;
import org.openapitools.model.AuthenticationRequest;
import org.openapitools.model.AuthenticationResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
//@SpringBootTest(classes = {TestConfig.class, StartApplication.class})
public abstract class BaseIntegrationTest {

    protected String getAuthToken() {
        RestTemplate restTemplate = new RestTemplate();
        TestRestTemplate testRestTemplate =  new TestRestTemplate();

        String uri = "https://localhost:7887/authenticate";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("aka");
        request.setPassword("password");

        HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, AuthenticationResponse.class);
        return responseEntity.getBody().getJwt();
    }

}
