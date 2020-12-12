package com.repositories.analyzer.service;

import com.repositories.analyzer.service.config.ServiceIntegrationTestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {
                ServiceIntegrationTestConfiguration.class
        }
)
public abstract class AbstractServiceIntegrationTest {

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}