package com.repositories.analyzer.service.repository.creation.bulk;

import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

final class ImmutableRepositoryDocumentBulkCreationParameter implements RepositoryDocumentBulkCreationParameter {

    private final Collection<RepositoryDocumentCreationParameter> repositories;

    ImmutableRepositoryDocumentBulkCreationParameter(final Collection<RepositoryDocumentCreationParameter> repositories) {
        this.repositories = repositories;
    }

    @Override
    public Collection<RepositoryDocumentCreationParameter> repositories() {
        return repositories;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDocumentBulkCreationParameter)) {
            return false;
        }
        final RepositoryDocumentBulkCreationParameter that = (RepositoryDocumentBulkCreationParameter) o;
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