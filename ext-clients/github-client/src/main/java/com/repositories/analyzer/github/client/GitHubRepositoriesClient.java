package com.repositories.analyzer.github.client;

import com.repositories.analyzer.github.client.model.request.GitHubRepositoryCommitsRequest;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryContributorsRequest;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommitsResponse;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryContributorsResponse;

public interface GitHubRepositoriesClient {

    GitHubRepositoryCommitsResponse retrieveCommits(GitHubRepositoryCommitsRequest request);

    GitHubRepositoryContributorsResponse retrieveContributors(GitHubRepositoryContributorsRequest request);
}