package com.urldedupe;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableBatchProcessing // enables Spring Batch features & provides a base configuration for setting up batch jobs
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class UrlDedupeApplication extends DefaultBatchConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(UrlDedupeApplication.class, args);
    }
}


