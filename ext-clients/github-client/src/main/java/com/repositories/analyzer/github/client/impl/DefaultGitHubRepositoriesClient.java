package com.repositories.analyzer.github.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.repositories.analyzer.github.client.GitHubRepositoriesClient;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryCommitsRequest;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryContributorsRequest;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommit;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommitsResponse;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryContributor;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryContributorsResponse;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
class DefaultGitHubRepositoriesClient implements GitHubRepositoriesClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultGitHubRepositoriesClient.class);

    private static final TypeReference<List<GitHubRepositoryCommit>> COMMITS_REF = new TypeReference<>() {};

    private static final TypeReference<List<GitHubRepositoryContributor>> CONTRIBUTORS_REF = new TypeReference<>() {};

    private final ResponseHandler responseHandler;

    private final GitHubRepositoriesFeignClient gitHubRepositoriesFeignClient;

    DefaultGitHubRepositoriesClient(
            final ResponseHandler responseHandler,
            final GitHubRepositoriesFeignClient gitHubRepositoriesFeignClient
    ) {
        this.responseHandler = responseHandler;
        this.gitHubRepositoriesFeignClient = gitHubRepositoriesFeignClient;
    }

    @Override
    public GitHubRepositoryCommitsResponse retrieveCommits(final GitHubRepositoryCommitsRequest request) {
        Assert.notNull(request, "Null was passed as an argument for parameter 'request'.");
        logger.debug("Retrieving repository commits from GitHub API. Request: {}.", request);
        final Response response = gitHubRepositoriesFeignClient.retrieveCommits(request.fullName(), request.itemsPerPage());
        return responseHandler.handle(response, COMMITS_REF, GitHubRepositoryCommitsResponse::new);
    }

    @Override
    public GitHubRepositoryContributorsResponse retrieveContributors(final GitHubRepositoryContributorsRequest request) {
        Assert.notNull(request, "Null was passed as an argument for parameter 'request'.");
        logger.debug("Retrieving repository contributors from GitHub API. Request: {}.", request);
        final Response response = gitHubRepositoriesFeignClient.retrieveContributors(request.fullName());
        return responseHandler.handle(response, CONTRIBUTORS_REF, GitHubRepositoryContributorsResponse::new);
    }
}