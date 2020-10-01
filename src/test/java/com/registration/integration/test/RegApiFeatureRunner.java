package com.registration.integration.test;


import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/feature/",
        glue = {"com.registration.integration.test.step"} // packages used for glue code, looked up in the classpath
        )// security
public class RegApiFeatureRunner {
}


