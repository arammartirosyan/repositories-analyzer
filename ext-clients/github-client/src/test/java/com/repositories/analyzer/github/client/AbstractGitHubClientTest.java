package com.repositories.analyzer.github.client;

import feign.Request;
import feign.Response;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractGitHubClientTest {

    public abstract void verifyNoMoreMocksInteractions();

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static Response response() {
        return response(HttpStatus.OK.value());
    }

    public static Response response(final int status) {
        return Response.builder()
                .status(status)
                .body(new byte[]{})
                .request(Request.create(Request.HttpMethod.GET, UUID.randomUUID().toString(), Map.of(), null))
                .headers(Collections.emptyMap())
                .build();
    }
}