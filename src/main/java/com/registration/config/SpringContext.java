package com.registration.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringContext {
    @Bean
    @ConfigurationProperties(prefix = "spring.jta.atomikos.datasource")
    public DataSource dataSourceOne() {
        return new AtomikosDataSourceBean();
    }


}