package com.repositories.analyzer;

import com.repositories.analyzer.api.facade.repository.RepositoryServiceFacade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AnalyzerApiApplicationTest {

    @Autowired
    private RepositoryServiceFacade repositoryServiceFacade;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(repositoryServiceFacade);
    }
}