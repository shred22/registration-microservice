package com.registration.integration.test;


import com.registration.StartApplication;
import com.registration.integration.test.config.TestConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest(classes = {TestConfig.class, StartApplication.class})
public abstract class BaseIntegrationTest {

}
