package com.oai3.integration.test.steps;

import com.oai3.integration.test.BaseIntegrationTest;
import cucumber.api.java8.En;
import net.serenitybdd.core.Serenity;
import org.junit.Assert;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public class RegistrationApiITestSteps extends BaseIntegrationTest implements En {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationApiITestSteps.class);
    private RegistrationRequest registrationRequest;
    @Value("${api.url}")
    private String endpoint;
    private RegistrationResponse registrationResponse;
    private HttpStatus statusCode;
    private HttpHeaders responseHeaders;

    public RegistrationApiITestSteps() {
        Given("^registration API request$", this::createRegistrationRequest);
        When("^the client calls service$", this::invokeService);
        Then("^verify expected result$", this::validateResult);
    }
    private void createRegistrationRequest() {
        LOGGER.info("----- Preparing Registration Request ------");
        registrationRequest = new RegistrationRequest();
        registrationRequest.setAge("25");
        registrationRequest.setName("Nautiyal");
    }
    private void invokeService() {
        LOGGER.info("----- Invoking REST Service -------");
        RestTemplate template = new RestTemplate();
        Map<String, String> vars = new HashMap<>();
        vars.put("regId", "42");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoint)
                .path("1124");

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", "custom-auth-token-shreyas");

        HttpEntity<RegistrationRequest> request = new HttpEntity<>(registrationRequest, headers);
        ResponseEntity<RegistrationResponse> registrationResponseResponseEntity = template.exchange(endpoint, HttpMethod.POST, request, RegistrationResponse.class, vars);
        registrationResponse = registrationResponseResponseEntity.getBody();
        statusCode = registrationResponseResponseEntity.getStatusCode();
        responseHeaders = registrationResponseResponseEntity.getHeaders();
        Serenity.recordReportData().withTitle("Registration API test Results").andContents(registrationResponse.toString());
    }
    private void validateResult() {
        LOGGER.info("-----In validateResult-----");
        Assert.assertNotNull(registrationResponse);
        Assert.assertNotNull(statusCode);
    }
}
