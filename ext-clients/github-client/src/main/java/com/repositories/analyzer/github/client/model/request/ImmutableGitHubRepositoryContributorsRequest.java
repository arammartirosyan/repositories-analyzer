package com.repositories.analyzer.github.client.model.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

final class ImmutableGitHubRepositoryContributorsRequest implements GitHubRepositoryContributorsRequest {

    private final String fullName;

    ImmutableGitHubRepositoryContributorsRequest(final String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitHubRepositoryContributorsRequest)) {
            return false;
        }
        final GitHubRepositoryContributorsRequest that = (GitHubRepositoryContributorsRequest) o;
        return new EqualsBuilder()
                .append(fullName(), that.fullName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(fullName())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fullName", fullName())
                .toString();
    }
}