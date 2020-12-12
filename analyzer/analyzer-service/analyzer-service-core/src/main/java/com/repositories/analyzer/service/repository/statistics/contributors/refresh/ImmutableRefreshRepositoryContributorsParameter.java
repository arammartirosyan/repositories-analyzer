package com.repositories.analyzer.service.repository.statistics.contributors.refresh;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

final class ImmutableRefreshRepositoryContributorsParameter implements RefreshRepositoryContributorsParameter {

    private final String repositoryUid;

    private final Collection<RefreshRepositoryContributorParameter> contributors;

    ImmutableRefreshRepositoryContributorsParameter(final String repositoryUid,
                                                    final Collection<RefreshRepositoryContributorParameter> contributors) {
        this.repositoryUid = repositoryUid;
        this.contributors = contributors;
    }

    @Override
    public String repositoryUid() {
        return repositoryUid;
    }

    @Override
    public Collection<RefreshRepositoryContributorParameter> contributors() {
        return contributors;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefreshRepositoryContributorsParameter)) {
            return false;
        }
        final RefreshRepositoryContributorsParameter that = (RefreshRepositoryContributorsParameter) o;
        return new EqualsBuilder()
                .append(repositoryUid, that.repositoryUid())
                .append(contributors, that.contributors())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(repositoryUid())
                .append(contributors())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("repositoryUid", repositoryUid())
                .append("contributors", contributors())
                .toString();
    }
}