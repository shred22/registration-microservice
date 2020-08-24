package com.registration.integration.test;


import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty", "json:build/reports/cucumber.json"},
        features = "src/test/resources/feature/",
        snippets = SnippetType.CAMELCASE,
        glue = {"com.registration.integration.test.steps"}, // packages used for glue code, looked up in the classpath
        tags = {"not @manual"} )// security
//@SpringBootTest(classes = TestConfig.class)
public class RegApiFeatureRunner {
}


