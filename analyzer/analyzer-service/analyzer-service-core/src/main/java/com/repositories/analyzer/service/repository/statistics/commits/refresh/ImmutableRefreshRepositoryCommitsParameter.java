package com.repositories.analyzer.service.repository.statistics.commits.refresh;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

final class ImmutableRefreshRepositoryCommitsParameter implements RefreshRepositoryCommitsParameter {

    private final String repositoryUid;

    private final Collection<RefreshRepositoryCommitParameter> commits;

    ImmutableRefreshRepositoryCommitsParameter(final String repositoryUid, final Collection<RefreshRepositoryCommitParameter> commits) {
        this.repositoryUid = repositoryUid;
        this.commits = commits;
    }

    @Override
    public String repositoryUid() {
        return repositoryUid;
    }

    @Override
    public Collection<RefreshRepositoryCommitParameter> commits() {
        return commits;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefreshRepositoryCommitsParameter)) {
            return false;
        }
        final RefreshRepositoryCommitsParameter that = (RefreshRepositoryCommitsParameter) o;
        return new EqualsBuilder()
                .append(repositoryUid(), that.repositoryUid())
                .append(commits(), that.commits())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(repositoryUid())
                .append(commits())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("repositoryUid", repositoryUid())
                .append("commits", commits())
                .toString();
    }
}