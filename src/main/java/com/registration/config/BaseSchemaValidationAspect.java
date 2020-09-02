package com.registration.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.registration.handler.error.SchemaValidationFailedException;
import com.registration.validation.BaseSchemaValidationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

@Aspect
@Configuration
public class BaseSchemaValidationAspect {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BaseSchemaValidationService schemaValidationService;
    private static final Logger logger = LoggerFactory.getLogger(BaseSchemaValidationAspect.class);

    enum RequestToApiFileMapping {

        RegistrationRequest_POST("registrationapi.yaml", "/register", "RegistrationRequest", HttpMethod.POST, new String[] {"authToken"}),
        RegistrationRequest_GET("registrationapi.yaml", "/register/{regId}","RegistrationRequest", HttpMethod.GET, new String[] {"authToken"}),
        RegistrationRequest_DELETE("registrationapi.yaml", "/register/{regId}","RegistrationRequest", HttpMethod.GET, new String[] {"authToken"}),
        RegistrationResponse("registrationapi.yaml", "/register", "RegistrationResponse", HttpMethod.POST),
        RegistrationDetailResponse("registrationapi.yaml", "/register/{regId}","RegistrationDetailResponse", HttpMethod.GET),
        AuthenticationRequest("authenticationapi.yaml", "/authenticate", "AuthenticationRequest", HttpMethod.POST),
        AuthenticationResponse("authenticationapi.yaml", "/authenticate", "AuthenticationResponse", HttpMethod.POST);

        private final HttpMethod method;
        private String apiPath;
        private String specFileName;
        private String[] headerNames;
        private String requestClassName;

        RequestToApiFileMapping(String specFileName, String apiPath, String requestClassName, HttpMethod method, String ... headerNames) {
            this.specFileName = specFileName;
            this.apiPath = apiPath;
            this.headerNames = headerNames;
            this.requestClassName = requestClassName;
            this.method = method;
        }

        RequestToApiFileMapping(String specFileName, String apiPath, String requestClassName, HttpMethod method) {
            this.specFileName = specFileName;
            this.apiPath = apiPath;
            this.requestClassName = requestClassName;
            this.method = method;
        }

        public String getApiPath() {
            return apiPath;
        }

        public String getSpecFileName() {
            return specFileName;
        }

        public String getRequestClassName() {
            return requestClassName;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public static RequestToApiFileMapping requestToEnum(String value) {
            List<RequestToApiFileMapping> apiFileMappings = asList(RequestToApiFileMapping.values());
            for(RequestToApiFileMapping mapping : apiFileMappings) {
                if(mapping.getRequestClassName().equals(value)) {
                    return mapping;
                }
            }
            throw new SchemaValidationFailedException("Invalid Request.");
        }
    }


    //What kind of method calls I would intercept
    //execution(* PACKAGE.*.*(..))
    @Before("execution(* com.registration.controller.*.*(..))")
    public void validateSchemaBeforeAPIProcessing(JoinPoint joinPoint) {
        //Advice
        //Exclude if args are empty and if the invoked method is of type HTTP GET

        if (isNotEmpty(joinPoint.getArgs()) && asList("Get", "Delete").stream().anyMatch(httpMthod -> httpMthod.contains(joinPoint.getSignature().getName()))) {
            logger.info("Schema Validation Initiated {}", joinPoint);
            try {
                RequestToApiFileMapping requestToApiFileMapping = RequestToApiFileMapping.requestToEnum(joinPoint.getArgs()[joinPoint.getArgs().length - 1].getClass().getSimpleName());
                Map<String, String> headerMap = new HashMap<>();
                for(int i=0; i < joinPoint.getArgs().length -1; i++) {
                    headerMap.put(requestToApiFileMapping.headerNames[i], joinPoint.getArgs()[i].toString());
                }
                schemaValidationService.validate(objectMapper.writeValueAsString(joinPoint.getArgs()[joinPoint.getArgs().length - 1]), requestToApiFileMapping.getSpecFileName(),
                        requestToApiFileMapping.getApiPath(), headerMap);
            } catch (JsonProcessingException e) {
                throw new SchemaValidationFailedException(e);
            }
        }
    }

    @AfterReturning(pointcut = "execution(* com.registration.controller.*.*(..))", returning = "retVal")
    public void validateSchemaAfterAPIProcessing(JoinPoint joinPoint, ResponseEntity<?> retVal) {
        //Advice
        //Exclude if return value is empty
        if (retVal.getBody() != null ) {
             logger.info("Initiating Response Schema  validation . {} ", joinPoint);
            try {
                RequestToApiFileMapping requestToApiFileMapping = RequestToApiFileMapping.requestToEnum(retVal.getBody().getClass().getSimpleName());
                schemaValidationService.validateResponse(retVal.getStatusCode(), objectMapper.writeValueAsString(retVal.getBody()), requestToApiFileMapping.getSpecFileName(),
                        requestToApiFileMapping.getApiPath(), requestToApiFileMapping.getMethod());
            } catch (JsonProcessingException e) {
                throw new SchemaValidationFailedException(e);
            }
        }
    }
}