package com.repositories.analyzer.service.repository.creation.bulk;

import com.repositories.analyzer.service.repository.details.RepositoryDetails;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.List;

final class ImmutableRepositoryDocumentBulkCreationResult implements RepositoryDocumentBulkCreationResult {

    private final Collection<RepositoryDetails> repositories;

    private final Collection<? extends RepositoryDocumentBulkCreationFailure> failures;

    ImmutableRepositoryDocumentBulkCreationResult(final Collection<RepositoryDetails> repositories) {
        this.repositories = repositories;
        this.failures = List.of();
    }

    @Override
    public Collection<RepositoryDetails> repositories() {
        return repositories;
    }

    @Override
    public Collection<? extends RepositoryDocumentBulkCreationFailure> failures() {
        return failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDocumentBulkCreationResult)) {
            return false;
        }
        final RepositoryDocumentBulkCreationResult that = (RepositoryDocumentBulkCreationResult) o;
        return new EqualsBuilder()
                .append(repositories(), that.repositories())
                .append(failures(), that.failures())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(repositories())
                .append(failures())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("repositories", repositories())
                .append("failures", failures())
                .toString();
    }
}