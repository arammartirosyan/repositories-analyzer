package com.repositories.analyzer.api.facade.repository.sync;

import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

final class ImmutableRepositoriesSyncParameter implements RepositoriesSyncParameter {

    private final Collection<RepositoryDocumentCreationParameter> reposToCreate;

    private final Collection<RepositoryDocumentModificationParameter> reposToModify;

    ImmutableRepositoriesSyncParameter(final Collection<RepositoryDocumentCreationParameter> reposToCreate,
                                       final Collection<RepositoryDocumentModificationParameter> reposToModify) {
        this.reposToCreate = reposToCreate;
        this.reposToModify = reposToModify;
    }

    @Override
    public Collection<RepositoryDocumentCreationParameter> reposToCreate() {
        return reposToCreate;
    }

    @Override
    public Collection<RepositoryDocumentModificationParameter> reposToModify() {
        return reposToModify;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoriesSyncParameter)) {
            return false;
        }
        final RepositoriesSyncParameter that = (RepositoriesSyncParameter) o;
        return new EqualsBuilder()
                .append(reposToCreate(), that.reposToCreate())
                .append(reposToModify(), that.reposToModify())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(reposToCreate())
                .append(reposToModify())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("reposToCreate", reposToCreate())
                .append("reposToModify", reposToModify())
                .toString();
    }
}