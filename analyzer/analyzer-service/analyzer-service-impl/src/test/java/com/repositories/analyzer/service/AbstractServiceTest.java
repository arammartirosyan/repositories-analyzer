package com.repositories.analyzer.service;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractServiceTest {

    public abstract void verifyNoMoreMocksInteractions();

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}