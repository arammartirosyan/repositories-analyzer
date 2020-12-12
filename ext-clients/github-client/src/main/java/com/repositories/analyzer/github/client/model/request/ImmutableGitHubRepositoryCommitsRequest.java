package com.repositories.analyzer.github.client.model.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

final class ImmutableGitHubRepositoryCommitsRequest implements GitHubRepositoryCommitsRequest {

    private final String fullName;

    private final int itemsPerPage;

    ImmutableGitHubRepositoryCommitsRequest(final String fullName, final int itemsPerPage) {
        this.fullName = fullName;
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    @Override
    public int itemsPerPage() {
        return itemsPerPage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitHubRepositoryCommitsRequest)) {
            return false;
        }
        final GitHubRepositoryCommitsRequest that = (GitHubRepositoryCommitsRequest) o;
        return new EqualsBuilder()
                .append(itemsPerPage(), that.itemsPerPage())
                .append(fullName(), that.fullName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(fullName())
                .append(itemsPerPage())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fullName", fullName())
                .append("itemsPerPage", itemsPerPage())
                .toString();
    }
}