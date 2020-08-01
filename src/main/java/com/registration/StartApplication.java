package com.registration;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"org.openapitools.api" ,"org.openapitools.configuration", "com.registration"})
@EnableSwagger2
@EnableCaching
@EnableTransactionManagement
@EnableConfigurationProperties
public class StartApplication {

    @Generated
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }


}
