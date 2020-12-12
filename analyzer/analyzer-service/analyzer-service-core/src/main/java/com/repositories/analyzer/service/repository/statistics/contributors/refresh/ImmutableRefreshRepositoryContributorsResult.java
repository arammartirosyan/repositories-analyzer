package com.repositories.analyzer.service.repository.statistics.contributors.refresh;

import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

final class ImmutableRefreshRepositoryContributorsResult implements RefreshRepositoryContributorsResult {

    private final Collection<RepositoryContributorDetails> contributors;

    private final Collection<? extends RefreshRepositoryContributorsFailure> failures;

    ImmutableRefreshRepositoryContributorsResult(
            final Collection<RepositoryContributorDetails> contributors,
            final Collection<? extends RefreshRepositoryContributorsFailure> failures
    ) {
        this.contributors = contributors;
        this.failures = failures;
    }

    @Override
    public Collection<RepositoryContributorDetails> contributors() {
        return contributors;
    }

    @Override
    public Collection<? extends RefreshRepositoryContributorsFailure> failures() {
        return failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefreshRepositoryContributorsResult)) {
            return false;
        }
        final RefreshRepositoryContributorsResult that = (RefreshRepositoryContributorsResult) o;
        return new EqualsBuilder()
                .append(contributors(), that.contributors())
                .append(failures(), that.failures())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(contributors())
                .append(failures())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("contributors", contributors())
                .append("failures", failures())
                .toString();
    }
}