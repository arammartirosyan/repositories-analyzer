package com.repositories.analyzer.api.facade;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractServiceFacadeTest {

    public abstract void verifyNoMoreMocksInteractions();

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}