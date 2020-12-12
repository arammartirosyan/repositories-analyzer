package com.repositories.analyzer.github.client.model.request;

import com.repositories.analyzer.github.client.model.common.GitHubSorting;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

final class ImmutableGitHubRepositoriesSearchRequest implements GitHubRepositoriesSearchRequest {

    private final String query;

    private final GitHubSorting sorting;

    ImmutableGitHubRepositoriesSearchRequest(final String query) {
        this.query = query;
        this.sorting = null;
    }

    ImmutableGitHubRepositoriesSearchRequest(final String query, final GitHubSorting sorting) {
        this.query = query;
        this.sorting = sorting;
    }

    @Override
    public String query() {
        return query;
    }

    @Override
    public Optional<GitHubSorting> sorting() {
        return Optional.ofNullable(sorting);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitHubRepositoriesSearchRequest)) {
            return false;
        }
        final GitHubRepositoriesSearchRequest that = (GitHubRepositoriesSearchRequest) o;
        return new EqualsBuilder()
                .append(query(), that.query())
                .append(sorting(), that.sorting())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(query())
                .append(sorting())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("query", query())
                .append("sorting", sorting())
                .toString();
    }
}