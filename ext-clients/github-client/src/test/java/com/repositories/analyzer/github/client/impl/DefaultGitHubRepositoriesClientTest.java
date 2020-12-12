package com.repositories.analyzer.github.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.repositories.analyzer.github.client.AbstractGitHubClientTest;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryCommitsRequest;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryContributorsRequest;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommitsResponse;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryContributorsResponse;
import feign.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class DefaultGitHubRepositoriesClientTest extends AbstractGitHubClientTest {

    @Mock
    private ResponseHandler responseHandler;

    @Mock
    private GitHubRepositoriesFeignClient gitHubRepositoriesFeignClient;

    @InjectMocks
    private DefaultGitHubRepositoriesClient gitHubRepositoriesClient;

    @After
    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(responseHandler, gitHubRepositoriesFeignClient);
    }

    @Test
    public void testRetrieveCommits() {
        final String fullName = uuid();
        final int itemsPerPage = 100;
        final GitHubRepositoryCommitsResponse commitsResponse = new GitHubRepositoryCommitsResponse();

        when(gitHubRepositoriesFeignClient.retrieveCommits(fullName, itemsPerPage)).thenReturn(response());
        when(responseHandler.handle(any(Response.class), any(TypeReference.class), any(Supplier.class))).thenReturn(commitsResponse);

        final GitHubRepositoryCommitsResponse response = gitHubRepositoriesClient.retrieveCommits(
                GitHubRepositoryCommitsRequest.of(fullName, itemsPerPage)
        );
        Assert.assertEquals(commitsResponse, response);

        verify(gitHubRepositoriesFeignClient).retrieveCommits(fullName, itemsPerPage);
        verify(responseHandler).handle(any(Response.class), any(TypeReference.class), any(Supplier.class));
    }

    @Test
    public void testRetrieveContributors() {
        final String fullName = uuid();

        final GitHubRepositoryContributorsResponse contributorsResponse = new GitHubRepositoryContributorsResponse();

        when(gitHubRepositoriesFeignClient.retrieveContributors(fullName)).thenReturn(response());
        when(responseHandler.handle(any(Response.class), any(TypeReference.class), any(Supplier.class))).thenReturn(contributorsResponse);

        final GitHubRepositoryContributorsResponse response = gitHubRepositoriesClient.retrieveContributors(
                GitHubRepositoryContributorsRequest.of(fullName)
        );
        Assert.assertEquals(contributorsResponse, response);

        verify(gitHubRepositoriesFeignClient).retrieveContributors(fullName);
        verify(responseHandler).handle(any(Response.class), any(TypeReference.class), any(Supplier.class));
    }
}