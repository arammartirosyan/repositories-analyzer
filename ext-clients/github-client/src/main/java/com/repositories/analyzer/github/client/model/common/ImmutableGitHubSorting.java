package com.repositories.analyzer.github.client.model.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

final class ImmutableGitHubSorting implements GitHubSorting {

    private final GitHubSortOrder order;

    private final GitHubSortProperty property;

    ImmutableGitHubSorting(final GitHubSortOrder order, final GitHubSortProperty property) {
        this.order = order;
        this.property = property;
    }

    @Override
    public GitHubSortOrder order() {
        return order;
    }

    @Override
    public GitHubSortProperty property() {
        return property;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitHubSorting)) {
            return false;
        }
        final GitHubSorting that = (GitHubSorting) o;
        return new EqualsBuilder()
                .append(order(), that.order())
                .append(property(), that.property())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(order())
                .append(property())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("order", order())
                .append("property", property())
                .toString();
    }
}