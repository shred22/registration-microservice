package com.registration.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "regservice.jwt")
@Getter
@Setter
public class JwtConfigurationProperties {

    private String keystorePassword;
    private String privatekeyPassword;
}
