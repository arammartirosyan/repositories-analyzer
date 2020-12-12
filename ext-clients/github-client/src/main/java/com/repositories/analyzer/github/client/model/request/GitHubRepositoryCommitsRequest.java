package com.repositories.analyzer.github.client.model.request;

import org.apache.commons.lang3.StringUtils;

public interface GitHubRepositoryCommitsRequest {

    String fullName();

    int itemsPerPage();

    static GitHubRepositoryCommitsRequest of(final String fullName, final int itemsPerPage) {
        if (StringUtils.isBlank(fullName)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'fullName'.");
        }
        if (itemsPerPage <= 0) {
            throw new IllegalArgumentException("Negative or zero was passed as an argument for parameter 'itemsPerPage'.");
        }
        return new ImmutableGitHubRepositoryCommitsRequest(fullName, itemsPerPage);
    }
}