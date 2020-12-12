package com.repositories.analyzer.api.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
class ExecutorConfiguration {

    @Bean("msCallsExecutor")
    Executor msCallsExecutor(
            @Value("${ms.calls.executor.core.pool.size:2}") final int corePoolSize,
            @Value("${ms.calls.executor.max.pool.size:16}") final int maxPoolSize,
            @Value("${ms.calls.executor.queue.capacity:256}") final int queueCapacity
    ) {
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        return threadPoolTaskExecutor;
    }
}