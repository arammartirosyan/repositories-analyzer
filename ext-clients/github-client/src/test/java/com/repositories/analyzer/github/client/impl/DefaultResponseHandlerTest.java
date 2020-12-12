package com.repositories.analyzer.github.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repositories.analyzer.github.client.AbstractGitHubClientTest;
import com.repositories.analyzer.github.client.exception.GitHubExternalClientException;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchItems;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultResponseHandlerTest extends AbstractGitHubClientTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DefaultResponseHandler responseHandler;

    @After
    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(objectMapper);
    }

    @Test
    public void testHandleSuccessResponse() throws IOException {
        final GitHubRepositoriesSearchItems searchItems = new GitHubRepositoriesSearchItems();

        final GitHubRepositoriesSearchResponse searchResponse = new GitHubRepositoriesSearchResponse();
        searchResponse.setResponse(searchItems);

        final TypeReference<GitHubRepositoriesSearchItems> typeReference = new TypeReference<>() {};

        when(objectMapper.readValue(any(InputStream.class), eq(typeReference))).thenReturn(searchItems);

        final GitHubRepositoriesSearchResponse response = responseHandler.handle(
                response(HttpStatus.OK.value()), typeReference, GitHubRepositoriesSearchResponse::new
        );
        Assert.assertFalse(response.isThreshHoldReached());
        Assert.assertEquals(searchResponse, response);

        verify(objectMapper).readValue(any(InputStream.class), eq(typeReference));
    }

    @Test
    public void testHandleForbiddenResponse() {
        final TypeReference<GitHubRepositoriesSearchItems> typeReference = new TypeReference<>() {};

        final GitHubRepositoriesSearchResponse response = responseHandler.handle(
                response(HttpStatus.FORBIDDEN.value()), typeReference, GitHubRepositoriesSearchResponse::new
        );
        Assert.assertTrue(response.isThreshHoldReached());
    }

    @Test
    public void testHandleUnexpectedResponse() {
        final TypeReference<GitHubRepositoriesSearchItems> typeReference = new TypeReference<>() {};
        try {
            responseHandler.handle(
                    response(HttpStatus.SERVICE_UNAVAILABLE.value()),
                    typeReference, GitHubRepositoriesSearchResponse::new
            );
            Assert.fail("Exception should be thrown.");
        } catch (final GitHubExternalClientException ignored) {
        }
    }
}