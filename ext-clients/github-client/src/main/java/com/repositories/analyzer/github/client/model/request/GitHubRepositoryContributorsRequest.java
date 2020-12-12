package com.repositories.analyzer.github.client.model.request;

import org.apache.commons.lang3.StringUtils;

public interface GitHubRepositoryContributorsRequest {

    String fullName();

    static GitHubRepositoryContributorsRequest of(final String fullName) {
        if (StringUtils.isBlank(fullName)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'fullName'.");
        }
        return new ImmutableGitHubRepositoryContributorsRequest(fullName);
    }
}