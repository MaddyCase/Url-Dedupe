package com.urldedupe.config;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UrlDeduperJobConfig {

    private final JobBuilderFactory mJobBuilders;

    private final StepBuilderFactory mStepBuilders;

    @Autowired
    public UrlDeduperJobConfig(final JobBuilderFactory jobBuilders, final StepBuilderFactory stepBuilders) {
        mJobBuilders = jobBuilders;
        mStepBuilders = stepBuilders;
    }
}
