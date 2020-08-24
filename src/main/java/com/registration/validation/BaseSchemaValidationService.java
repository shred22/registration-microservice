package com.registration.validation;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.LevelResolverFactory;
import com.atlassian.oai.validator.report.ValidationReport;
import com.registration.handler.error.SchemaValidationFailedException;
import com.registration.handler.error.SchemaValidationFailureDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BaseSchemaValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(BaseSchemaValidationService.class);

    // Request Validation
    public void validate(String requestBody, String specFileName, String apiPath, Map<String, String> headerMap) {

        final OpenApiInteractionValidator validator = buildValidator(specFileName);
        SimpleRequest.Builder requestBuilder = SimpleRequest.Builder.post(apiPath).withBody(requestBody)
                .withContentType(MediaType.APPLICATION_JSON.toString());

        headerMap.keySet().stream().forEach(key -> requestBuilder.withHeader(key, headerMap.get(key)));
        ValidationReport validationReport = validator.validateRequest(requestBuilder.build());
        final List<SchemaValidationFailureDetails> failureDetails = new ArrayList<>();
        if (validationReport.hasErrors()) {
            processValidationResult(validationReport, failureDetails);
            LOG.error("Schema Validation has failed with Error(s) : " + failureDetails);
            throw new SchemaValidationFailedException(failureDetails);
        }
    }

    // Response Validation
    public void validateResponse(HttpStatus statusCode, String responseBody, String specFileName, String apiPath, HttpMethod method) {
        final OpenApiInteractionValidator validator = buildValidator(specFileName);
        SimpleResponse response = SimpleResponse.Builder.status(statusCode.value()).withBody(responseBody)
                .withContentType(MediaType.APPLICATION_JSON.toString()).build();

        ValidationReport validationReport = validator.validateResponse(apiPath, Request.Method.valueOf(method.name()), response);
        final List<SchemaValidationFailureDetails> failureDetails = new ArrayList<>();
        if (validationReport.hasErrors()) {
            processValidationResult(validationReport, failureDetails);
            LOG.error("Response schema Validation has failed with Error(s) : " + failureDetails);
            throw new SchemaValidationFailedException(failureDetails);
        }
    }

    private OpenApiInteractionValidator buildValidator(String specFileName) {
        String path = null;

        ClassPathResource cpr = new ClassPathResource("apispecs/" + specFileName);
        //File file = ResourceUtils.getFile("classpath:apispecs/" + specFileName);
        path = cpr.getPath();


        LOG.info("Loaded File for validation from path : " + path);
        return OpenApiInteractionValidator.createFor(path)
                .withLevelResolver(LevelResolverFactory.withAdditionalPropertiesIgnored())
                .build();
    }

    private void processValidationResult(ValidationReport validationReport, List<SchemaValidationFailureDetails> failureDetails) {
        validationReport.getMessages().stream().forEach(message -> {
            failureDetails.add(new SchemaValidationFailureDetails(message.getMessage(), message.getContext().orElse(null).getRequestPath().orElse(null),
                    message.getKey()));
        });
    }
}
