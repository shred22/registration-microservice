package com.oai3.integration.test.config;

import com.oai3.integration.test.steps.RegistrationApiITestSteps;
import cucumber.api.Scenario;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class CucumberHooks implements En {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationApiITestSteps.class);

    public CucumberHooks() {
        Before((Scenario scenario) -> {
            LOGGER.info("---- before scenario  ----");
        });
        After((Scenario scenario) -> {
            LOGGER.info("---- after scenario ----");
        });
    }
}