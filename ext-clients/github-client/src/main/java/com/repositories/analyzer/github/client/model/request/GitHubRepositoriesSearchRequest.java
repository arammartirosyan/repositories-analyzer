package com.repositories.analyzer.github.client.model.request;

import com.repositories.analyzer.github.client.model.common.GitHubSorting;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public interface GitHubRepositoriesSearchRequest {

    String query();

    Optional<GitHubSorting> sorting();

    static GitHubRepositoriesSearchRequest of(final String query) {
        if (StringUtils.isBlank(query)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'query'.");
        }
        return new ImmutableGitHubRepositoriesSearchRequest(query);
    }

    static GitHubRepositoriesSearchRequest of(final String query, final GitHubSorting sorting) {
        if (StringUtils.isBlank(query)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'query'.");
        }
        if (sorting == null) {
            throw new IllegalArgumentException("Null was passed as an argument for parameter 'sorting'.");
        }
        return new ImmutableGitHubRepositoriesSearchRequest(query, sorting);
    }
}