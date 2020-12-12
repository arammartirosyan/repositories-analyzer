package com.repositories.analyzer.api.gateway.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients({"com.repositories.analyzer.api.client"})
class ApiGatewayConfiguration {
}