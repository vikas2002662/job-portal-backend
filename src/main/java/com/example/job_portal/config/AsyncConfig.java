package com.example.job_portal.config;


import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);     // minimum threads
        executor.setMaxPoolSize(10);     // max threads
        executor.setQueueCapacity(100);  // queue size
        executor.setThreadNamePrefix("Async-");

        executor.initialize();

        return executor;
    }
}