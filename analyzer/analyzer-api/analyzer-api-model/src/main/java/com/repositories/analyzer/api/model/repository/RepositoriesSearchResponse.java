package com.repositories.analyzer.api.model.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repositories.analyzer.common.api.model.AbstractResponse;
import com.repositories.analyzer.common.usecase.Failure;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

public class RepositoriesSearchResponse extends AbstractResponse {

    @JsonProperty
    private Collection<RepositoryDto> repositories;

    public RepositoriesSearchResponse() {
        super();
    }

    public RepositoriesSearchResponse(final Collection<? extends Failure> failures) {
        super(failures);
    }

    public Collection<RepositoryDto> getRepositories() {
        return repositories;
    }

    public void setRepositories(final Collection<RepositoryDto> repositories) {
        this.repositories = repositories;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoriesSearchResponse)) {
            return false;
        }
        final RepositoriesSearchResponse that = (RepositoriesSearchResponse) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getRepositories(), that.getRepositories())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getRepositories())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("repositories", getRepositories())
                .toString();
    }
}