package com.repositories.analyzer.service.repository.details;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.List;

final class ImmutableRetrieveRepositoryDetailsResult implements RetrieveRepositoryDetailsResult {

    private final RepositoryDetails details;

    private final Collection<? extends RetrieveRepositoryDetailsFailure> failures;

    ImmutableRetrieveRepositoryDetailsResult(final RepositoryDetails details) {
        this.details = details;
        this.failures = List.of();
    }

    ImmutableRetrieveRepositoryDetailsResult(final Collection<? extends RetrieveRepositoryDetailsFailure> failures) {
        this.details = null;
        this.failures = failures;
    }

    @Override
    public RepositoryDetails details() {
        return details;
    }

    @Override
    public Collection<? extends RetrieveRepositoryDetailsFailure> failures() {
        return failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetrieveRepositoryDetailsResult)) {
            return false;
        }
        final RetrieveRepositoryDetailsResult that = (RetrieveRepositoryDetailsResult) o;
        return new EqualsBuilder()
                .append(details(), that.details())
                .append(failures(), that.failures())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(details())
                .append(failures())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("details", details())
                .append("failures", failures())
                .toString();
    }
}