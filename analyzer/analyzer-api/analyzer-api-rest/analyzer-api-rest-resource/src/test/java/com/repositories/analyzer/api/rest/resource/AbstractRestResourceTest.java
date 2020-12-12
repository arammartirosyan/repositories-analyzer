package com.repositories.analyzer.api.rest.resource;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractRestResourceTest {

    public abstract void verifyNoMoreMocksInteractions();

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}