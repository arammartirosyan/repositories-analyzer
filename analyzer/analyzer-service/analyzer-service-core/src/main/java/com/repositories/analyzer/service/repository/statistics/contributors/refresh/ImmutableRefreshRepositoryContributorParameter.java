package com.repositories.analyzer.service.repository.statistics.contributors.refresh;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

final class ImmutableRefreshRepositoryContributorParameter implements RefreshRepositoryContributorParameter {

    private final String remoteId;

    private final String login;

    private final String type;

    ImmutableRefreshRepositoryContributorParameter(final String remoteId, final String login, final String type) {
        this.remoteId = remoteId;
        this.login = login;
        this.type = type;
    }

    @Override
    public String remoteId() {
        return remoteId;
    }

    @Override
    public String login() {
        return login;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefreshRepositoryContributorParameter)) {
            return false;
        }
        final RefreshRepositoryContributorParameter that = (RefreshRepositoryContributorParameter) o;
        return new EqualsBuilder()
                .append(remoteId(), that.remoteId())
                .append(login(), that.login())
                .append(type(), that.type())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(remoteId())
                .append(login())
                .append(type())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("remoteId", remoteId())
                .append("login", login())
                .append("type", type())
                .toString();
    }
}