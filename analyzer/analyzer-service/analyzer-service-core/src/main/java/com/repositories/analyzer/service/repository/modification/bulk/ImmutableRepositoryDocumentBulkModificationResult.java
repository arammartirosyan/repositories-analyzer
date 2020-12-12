package com.repositories.analyzer.service.repository.modification.bulk;

import com.repositories.analyzer.service.repository.details.RepositoryDetails;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.List;

final class ImmutableRepositoryDocumentBulkModificationResult implements RepositoryDocumentBulkModificationResult {

    private final Collection<RepositoryDetails> repositories;

    private final Collection<? extends RepositoryDocumentBulkModificationFailure> failures;

    ImmutableRepositoryDocumentBulkModificationResult(final Collection<RepositoryDetails> repositories) {
        this.repositories = repositories;
        this.failures = List.of();
    }

    @Override
    public Collection<RepositoryDetails> repositories() {
        return repositories;
    }

    @Override
    public Collection<? extends RepositoryDocumentBulkModificationFailure> failures() {
        return failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDocumentBulkModificationResult)) {
            return false;
        }
        final RepositoryDocumentBulkModificationResult that = (RepositoryDocumentBulkModificationResult) o;
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