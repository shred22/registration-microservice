package com.registration.integration.test.step;

import com.registration.integration.test.BaseIntegrationTest;
import com.registration.request.factories.RegistrationApiRequestFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import org.junit.Assert;
import org.openapitools.model.RegistrationDetailResponse;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RegistrationApiFeatureSteps extends BaseIntegrationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationApiITestSteps.class);
    private RegistrationRequest registrationRequest;
    @Value("${registration.api.url}")
    private String endpoint;
    @Autowired
    private RegistrationApiRequestFactory requestFactory = new RegistrationApiRequestFactory();
    private RegistrationResponse registrationResponse;
    private HttpStatus statusCode;
    private HttpHeaders responseHeaders;
    private static String jwt;
    private static Long registrationId;
    private RegistrationDetailResponse registrationDetailResponse;

    @Given("registration API request")
    public void registrationAPIRequest() {
        LOGGER.info("----- Preparing Registration Request ------");
        registrationRequest = requestFactory.createRegistrationRequest();
    }

    @When("the client calls service")
    public void theClientCallsService() {
        jwt = getAuthToken();
        HttpHeaders headers;

        LOGGER.info("----- Invoking REST Service -------");
        RestTemplate template = new RestTemplate();
        headers = new HttpHeaders();
        headers.add("authToken", "Bearer " + jwt);
        HttpEntity<RegistrationRequest> registrationRequestHttpEntity = new HttpEntity<>(registrationRequest, headers);
        LOGGER.info("Invoking Registration Service with Request {} and Endpoint {} : ", registrationRequest.toString(), "http://localhost:7887/register");
        ResponseEntity<RegistrationResponse> registrationResponseResponseEntity = template.exchange("http://localhost:7887/register", HttpMethod.POST, registrationRequestHttpEntity, RegistrationResponse.class);
        registrationResponse = registrationResponseResponseEntity.getBody();
        registrationId = registrationResponse.getRegistrationId();
        statusCode = registrationResponseResponseEntity.getStatusCode();
        responseHeaders = registrationResponseResponseEntity.getHeaders();
        Serenity.recordReportData().withTitle("Registration API test Results").andContents(registrationResponse.toString());
    }

    @Then("verify expected result")
    public void verifyExpectedResult() {
        LOGGER.info("-----In validateResult-----");
        Assert.assertNotNull(registrationResponse);
        Assert.assertNotNull(statusCode);
    }

    @Given("get registration API details")
    public void getRegistrationAPIDetails() {
        LOGGER.info("----- Preparing Registration Request ------");
        registrationRequest = requestFactory.createRegistrationRequest();
    }

    @When("the client calls service to get details")
    public void theClientCallsServiceToGetDetails() {
        LOGGER.info("----- Invoking GET REST Service -------");
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", "Bearer " + jwt);
        HttpEntity registrationRequestHttpEntity = new HttpEntity(registrationRequest, headers);
        LOGGER.info("Invoking Registration Service with Request {} and Endpoint {} : ", registrationRequest.toString(), "http://localhost:7887/register");
        ResponseEntity<RegistrationDetailResponse> responseEntity = template.exchange("http://localhost:7887/register/{regId}", HttpMethod.GET, registrationRequestHttpEntity, RegistrationDetailResponse.class, Map.of("regId", registrationId));
        registrationDetailResponse = responseEntity.getBody();
        statusCode = responseEntity.getStatusCode();
        Serenity.recordReportData().withTitle("Registration API test Results").andContents(registrationDetailResponse.toString());
    }

    @Then("verify expected result with registration details")
    public void verifyExpectedResultWithRegistrationDetails() {
        LOGGER.info("-----In validateResult-----");
        Assert.assertNotNull(registrationDetailResponse);
        Assert.assertNotNull(statusCode);
    }
}
