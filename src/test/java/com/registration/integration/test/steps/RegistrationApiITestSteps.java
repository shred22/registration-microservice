package com.registration.integration.test.steps;

import com.registration.integration.test.BaseIntegrationTest;
import com.registration.request.factories.RegistrationApiRequestFactory;
import cucumber.api.java8.En;
import net.serenitybdd.core.Serenity;
import org.junit.Assert;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RegistrationApiITestSteps extends BaseIntegrationTest implements En {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationApiITestSteps.class);
    private RegistrationRequest registrationRequest;
    @Value("${registration.api.url}")
    private String endpoint;
    @Autowired
    private RegistrationApiRequestFactory requestFactory = new RegistrationApiRequestFactory();
    private RegistrationResponse registrationResponse;
    private HttpStatus statusCode;
    private HttpHeaders responseHeaders;
    private String jwt;

    /*@ClassRule
    public static GenericContainer simpleWebServer
            = new GenericContainer("redis:alpine")
            .withExposedPorts(6479)
            .withNetwork(Network.builder() .driver("bridge").build())
            .withCommand("/bin/sh", "-c", "while true; do echo "
                    + "\"HTTP/1.1 200 OK\n\nHello World!\" | nc -l -p 80; done");*/

    public RegistrationApiITestSteps() {
        Given("^registration API request$", this::createRegistrationRequest);
        When("^the client calls service$", this::invokeService);
        Then("^verify expected result$", this::validateResult);
    }
    private void createRegistrationRequest() {
        LOGGER.info("----- Preparing Registration Request ------");
        registrationRequest = requestFactory.createRegistrationRequest();
    }
    private void invokeService() {

        String jwt = getAuthToken();
        HttpHeaders headers;

        LOGGER.info("----- Invoking REST Service -------");
        RestTemplate template = new RestTemplate();
        headers = new HttpHeaders();
        headers.add("authToken", "Bearer "+jwt);
        HttpEntity<RegistrationRequest> registrationRequestHttpEntity = new HttpEntity<>(registrationRequest, headers);
        LOGGER.info("Invoking Registration Service with Request {} and Endpoint {} : ", registrationRequest.toString(), "http://localhost:7887/register");
        ResponseEntity<RegistrationResponse> registrationResponseResponseEntity = template.exchange("http://localhost:7887/register", HttpMethod.POST, registrationRequestHttpEntity, RegistrationResponse.class);
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
