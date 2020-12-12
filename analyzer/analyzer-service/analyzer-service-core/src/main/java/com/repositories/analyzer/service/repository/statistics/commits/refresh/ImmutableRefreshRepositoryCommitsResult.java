package com.repositories.analyzer.service.repository.statistics.commits.refresh;

import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

final class ImmutableRefreshRepositoryCommitsResult implements RefreshRepositoryCommitsResult {

    private final Collection<RepositoryCommitDetails> commits;

    private final Collection<? extends RefreshRepositoryCommitsFailure> failures;

    ImmutableRefreshRepositoryCommitsResult(final Collection<RepositoryCommitDetails> commits,
                                            final Collection<? extends RefreshRepositoryCommitsFailure> failures) {
        this.commits = commits;
        this.failures = failures;
    }

    @Override
    public Collection<RepositoryCommitDetails> commits() {
        return commits;
    }

    @Override
    public Collection<? extends RefreshRepositoryCommitsFailure> failures() {
        return failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImmutableRefreshRepositoryCommitsResult)) {
            return false;
        }
        final ImmutableRefreshRepositoryCommitsResult that = (ImmutableRefreshRepositoryCommitsResult) o;
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