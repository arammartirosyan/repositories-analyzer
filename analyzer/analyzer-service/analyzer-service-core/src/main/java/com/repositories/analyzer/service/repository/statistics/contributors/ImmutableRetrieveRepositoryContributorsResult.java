package com.repositories.analyzer.service.repository.statistics.contributors;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.List;

final class ImmutableRetrieveRepositoryContributorsResult implements RetrieveRepositoryContributorsResult {

    private final Collection<RepositoryContributorDetails> contributors;

    private final Collection<? extends RetrieveRepositoryContributorsFailure> failures;

    ImmutableRetrieveRepositoryContributorsResult(final Collection<RepositoryContributorDetails> contributors) {
        this.contributors = contributors;
        this.failures = List.of();
    }

    @Override
    public Collection<RepositoryContributorDetails> contributors() {
        return contributors;
    }

    @Override
    public Collection<? extends RetrieveRepositoryContributorsFailure> failures() {
        return failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetrieveRepositoryContributorsResult)) {
            return false;
        }
        final RetrieveRepositoryContributorsResult that = (RetrieveRepositoryContributorsResult) o;
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