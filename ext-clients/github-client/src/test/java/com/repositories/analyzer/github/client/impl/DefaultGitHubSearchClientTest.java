package com.repositories.analyzer.github.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.repositories.analyzer.github.client.AbstractGitHubClientTest;
import com.repositories.analyzer.github.client.model.common.GitHubSortOrder;
import com.repositories.analyzer.github.client.model.common.GitHubSortProperty;
import com.repositories.analyzer.github.client.model.common.GitHubSorting;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoriesSearchRequest;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchResponse;
import feign.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class DefaultGitHubSearchClientTest extends AbstractGitHubClientTest {

    @Mock
    private ResponseHandler responseHandler;

    @Mock
    private GitHubSearchFeignClient gitHubSearchFeignClient;

    @InjectMocks
    private DefaultGitHubSearchClient gitHubSearchClient;

    @After
    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(responseHandler, gitHubSearchFeignClient);
    }

    @Test
    public void testSearch() {
        final String query = uuid();

        final GitHubRepositoriesSearchResponse searchResponse = new GitHubRepositoriesSearchResponse();

        when(gitHubSearchFeignClient.search(query)).thenReturn(response());
        when(responseHandler.handle(any(Response.class), any(TypeReference.class), any(Supplier.class))).thenReturn(searchResponse);

        final GitHubRepositoriesSearchResponse response = gitHubSearchClient.search(GitHubRepositoriesSearchRequest.of(query));
        Assert.assertEquals(searchResponse, response);

        verify(gitHubSearchFeignClient).search(query);
        verify(responseHandler).handle(any(Response.class), any(TypeReference.class), any(Supplier.class));
    }

    @Test
    public void testSearchWithSorting() {
        final String query = uuid();
        final GitHubSortOrder order = GitHubSortOrder.DESC;
        final GitHubSortProperty property = GitHubSortProperty.STARS;

        final GitHubRepositoriesSearchResponse searchResponse = new GitHubRepositoriesSearchResponse();

        when(gitHubSearchFeignClient.search(query, property.getValue(), order.getValue())).thenReturn(response());
        when(responseHandler.handle(any(Response.class), any(TypeReference.class), any(Supplier.class))).thenReturn(searchResponse);

        final GitHubRepositoriesSearchResponse response = gitHubSearchClient.search(
                GitHubRepositoriesSearchRequest.of(query, GitHubSorting.of(order, property))
        );
        Assert.assertEquals(searchResponse, response);

        verify(gitHubSearchFeignClient).search(query, property.getValue(), order.getValue());
        verify(responseHandler).handle(any(Response.class), any(TypeReference.class), any(Supplier.class));
    }
}