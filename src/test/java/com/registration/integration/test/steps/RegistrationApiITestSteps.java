package com.registration.integration.test.steps;

import com.registration.integration.test.BaseIntegrationTest;
import com.registration.integration.test.config.Context;
import com.registration.integration.test.config.ScenarioContext;
import com.registration.request.factories.RegistrationApiRequestFactory;
import cucumber.api.java8.En;
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
    private static String jwt;
    private static Long registrationId;
    private RegistrationDetailResponse registrationDetailResponse;
    private ScenarioContext context = new ScenarioContext();


    public RegistrationApiITestSteps() {
        Given("^registration API request$", this::createRegistrationRequest);
        When("^the client calls service$", this::invokeService);
        Then("^verify expected result$", this::validateResult);

        Given("^get registration API details$", this::createGetRegistrationRequest);
        When("^the client calls service to get details$", this::invokeGetService);
        Then("^verify expected result with registration details$", this::validateDetailsResult);
    }

    private void createRegistrationRequest() {
        LOGGER.info("----- Preparing Registration Request ------");
        registrationRequest = requestFactory.createRegistrationRequest();
    }

    private void createGetRegistrationRequest() {
        LOGGER.info("----- Preparing Registration Request ------");
        registrationRequest = requestFactory.createRegistrationRequest();


    }

    private void invokeGetService() {

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

    private void invokeService() {

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
        context.setContext(Context.REG_ID, registrationId);
        statusCode = registrationResponseResponseEntity.getStatusCode();
        responseHeaders = registrationResponseResponseEntity.getHeaders();
        Serenity.recordReportData().withTitle("Registration API test Results").andContents(registrationResponse.toString());
    }

    private void validateDetailsResult() {
        LOGGER.info("-----In validateResult-----");
        Assert.assertNotNull(registrationDetailResponse);
        Assert.assertNotNull(statusCode);
    }

    private void validateResult() {
        LOGGER.info("-----In validateResult-----");
        Assert.assertNotNull(registrationResponse);
        Assert.assertNotNull(statusCode);
    }
}
