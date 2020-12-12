package com.repositories.analyzer.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients({"com.repositories.analyzer.github.client"})
public class AnalyzerApiConfiguration {
}