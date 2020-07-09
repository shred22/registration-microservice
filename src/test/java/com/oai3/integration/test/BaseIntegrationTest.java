package com.oai3.integration.test;


import com.oai3.StartApplication;
import com.oai3.integration.test.config.TestConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest(classes = {TestConfig.class, StartApplication.class})
public abstract class BaseIntegrationTest {

}
