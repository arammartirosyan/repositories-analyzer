package com.repositories.analyzer.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.repositories.analyzer")
@PropertySources(@PropertySource("/integrationtest.properties"))
@EnableMongoRepositories("com.repositories.analyzer.persistence")
public class ServiceIntegrationTestConfiguration {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Lazy(value = false)
    @Bean(initMethod = "start", destroyMethod = "stop")
    public GenericContainer<MongoDBContainer> mongoDBContainer(@Value("${mongodb.port}") final int mongoDbPort,
                                                               @Value("${mongodb.docker.image.name}") final String mongoDbDockerImageName) {
        return new GenericContainer<MongoDBContainer>(mongoDbDockerImageName)
                .withExposedPorts(mongoDbPort);
    }
}