package com.registration.integration.test;


import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.openapitools.model.AuthenticationRequest;
import org.openapitools.model.AuthenticationResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.List;

public abstract class BaseIntegrationTest {

    public static void main(String[] args) {
        testIt();
    }

    private Resource trustStore = new ClassPathResource("ssl-httprest-client.jks");
    private String trustStorePassword = "storepassword";
    protected RestTemplate restTemplate() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
                .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

    protected String getAuthToken() throws Exception {
        RestTemplate restTemplate = restTemplate();
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

    public static void testIt() {
        List<String> strings = null;


        for(String s : strings) {
            System.out.println(" : : "+s);
        }

    }

}
