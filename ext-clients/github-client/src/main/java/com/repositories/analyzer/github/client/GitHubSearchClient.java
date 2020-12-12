package com.repositories.analyzer.github.client;

import com.repositories.analyzer.github.client.model.request.GitHubRepositoriesSearchRequest;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchResponse;

public interface GitHubSearchClient {

    GitHubRepositoriesSearchResponse search(GitHubRepositoriesSearchRequest searchRequest);
}