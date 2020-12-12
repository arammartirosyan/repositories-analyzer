package com.repositories.analyzer.service.repository.modification.bulk;

import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

final class ImmutableRepositoryDocumentBulkModificationParameter implements RepositoryDocumentBulkModificationParameter {

    private final Collection<RepositoryDocumentModificationParameter> repositories;

    ImmutableRepositoryDocumentBulkModificationParameter(final Collection<RepositoryDocumentModificationParameter> repositories) {
        this.repositories = repositories;
    }

    @Override
    public Collection<RepositoryDocumentModificationParameter> repositories() {
        return repositories;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDocumentBulkModificationParameter)) {
            return false;
        }
        final RepositoryDocumentBulkModificationParameter that = (RepositoryDocumentBulkModificationParameter) o;
        return new EqualsBuilder()
                .append(repositories(), that.repositories())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(repositories())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("repositories", repositories())
                .toString();
    }
}