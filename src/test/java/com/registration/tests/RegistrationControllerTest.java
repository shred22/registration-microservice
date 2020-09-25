package com.registration.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.registration.StartApplication;
import com.registration.request.factories.RegistrationApiRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openapitools.model.AuthenticationRequest;
import org.openapitools.model.AuthenticationResponse;
import org.openapitools.model.RegistrationRequest;
import org.openapitools.model.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ StartApplication.class })
@Slf4j
public class RegistrationControllerTest {
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private ObjectMapper mapper;
  @Autowired
  private RegistrationApiRequestFactory requestFactory;
  @Value("${endpoints.apis.authentication}")
  private String authEndpoint;
  @Value("${endpoints.apis.registration}")
  private String regEndpoint;


  @Before
  public void setUp() {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void shouldRegisterSuccessfully() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("aka");
    request.setPassword("password");

    MvcResult authResult = mockMvc.perform(post(authEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
    AuthenticationResponse authenticationResponse = mapper.readValue(authResult.getResponse().getContentAsString(), AuthenticationResponse.class);


    RegistrationRequest registrationRequest = requestFactory.createRegistrationRequest();

    MvcResult mvcResult = mockMvc.perform(post(regEndpoint)
            .header("authToken", "Bearer "+authenticationResponse.getJwt())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(registrationRequest))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated()).andReturn();

    log.info(mapper.writeValueAsString(mvcResult.getResponse().getContentAsString()));
    RegistrationResponse registrationResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), RegistrationResponse.class);
    assertAll(() -> assertNotNull(mvcResult.getResponse().getContentAsString()),
            () -> assertNotNull(registrationResponse.getRegistrationId()),
            () -> assertEquals(RegistrationResponse.StatusEnum.ACTIVE, registrationResponse.getStatus()));
  }
}