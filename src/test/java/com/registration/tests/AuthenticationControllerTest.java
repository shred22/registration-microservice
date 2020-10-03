package com.registration.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.registration.StartApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.openapitools.model.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ StartApplication.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthenticationControllerTest {
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private ObjectMapper mapper;
  @Value("${endpoints.apis.authentication}")
  private String endpoint;


  @Before
  public void setUp() {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void shouldAuthenticateSuccessfully() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("aka");
    request.setPassword("password");

    MvcResult mvcResult = mockMvc.perform(post(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

    assertAll(() -> Assertions.assertNotNull(mvcResult.getResponse().getContentAsString()));
  }
}