package com.repositories.analyzer.service.repository.statistics.commits;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.List;

final class ImmutableRetrieveRepositoryCommitsResult implements RetrieveRepositoryCommitsResult {

    private final Collection<RepositoryCommitDetails> commits;

    private final Collection<? extends RetrieveRepositoryCommitsFailure> failures;

    ImmutableRetrieveRepositoryCommitsResult(final Collection<RepositoryCommitDetails> commits) {
        this.commits = commits;
        this.failures = List.of();
    }

    @Override
    public Collection<RepositoryCommitDetails> commits() {
        return commits;
    }

    @Override
    public Collection<? extends RetrieveRepositoryCommitsFailure> failures() {
        return failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetrieveRepositoryCommitsResult)) {
            return false;
        }
        final RetrieveRepositoryCommitsResult that = (RetrieveRepositoryCommitsResult) o;
        return new EqualsBuilder()
                .append(commits(), that.commits())
                .append(failures(), that.failures())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(commits())
                .append(failures())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("commits", commits())
                .append("failures", failures())
                .toString();
    }
}