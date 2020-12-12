package com.repositories.analyzer.github.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.repositories.analyzer.github.client.GitHubSearchClient;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoriesSearchRequest;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchItems;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchResponse;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
class DefaultGitHubSearchClient implements GitHubSearchClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultGitHubSearchClient.class);

    private static final TypeReference<GitHubRepositoriesSearchItems> SEARCH_REF = new TypeReference<>() {};

    private final ResponseHandler responseHandler;

    private final GitHubSearchFeignClient gitHubSearchFeignClient;

    DefaultGitHubSearchClient(
            final ResponseHandler responseHandler,
            final GitHubSearchFeignClient gitHubSearchFeignClient
    ) {
        this.responseHandler = responseHandler;
        this.gitHubSearchFeignClient = gitHubSearchFeignClient;
    }

    @Override
    public GitHubRepositoriesSearchResponse search(final GitHubRepositoriesSearchRequest request) {
        Assert.notNull(request, "Null was passed as an argument for parameter 'request'.");
        logger.debug("Searching repositories from GitHub API. Request: {}.", request);
        final Response response = request.sorting()
                .map(sorting ->
                        gitHubSearchFeignClient.search(
                                request.query(), sorting.property().getValue(), sorting.order().getValue()
                        )
                )
                .orElseGet(() -> gitHubSearchFeignClient.search(request.query()));
        return responseHandler.handle(response, SEARCH_REF, GitHubRepositoriesSearchResponse::new);
    }
}