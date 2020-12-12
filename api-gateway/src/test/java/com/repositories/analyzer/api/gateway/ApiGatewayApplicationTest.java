package com.repositories.analyzer.api.gateway;

import com.repositories.analyzer.api.client.AnalyzerResourceClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiGatewayApplicationTest {

    @Autowired
    private AnalyzerResourceClient analyzerResourceClient;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(analyzerResourceClient);
    }
}