package com.repositories.analyzer.api.model.repository.contributors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repositories.analyzer.common.api.model.AbstractResponse;
import com.repositories.analyzer.common.usecase.Failure;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

public class RetrieveRepositoryContributorsResponse extends AbstractResponse {

    @JsonProperty
    private Collection<RepositoryContributorDto> contributors;

    public RetrieveRepositoryContributorsResponse() {
        super();
    }

    public RetrieveRepositoryContributorsResponse(final Collection<? extends Failure> failures) {
        super(failures);
    }

    public Collection<RepositoryContributorDto> getContributors() {
        return contributors;
    }

    public void setContributors(final Collection<RepositoryContributorDto> contributors) {
        this.contributors = contributors;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetrieveRepositoryContributorsResponse)) {
            return false;
        }
        final RetrieveRepositoryContributorsResponse that = (RetrieveRepositoryContributorsResponse) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getContributors(), that.getContributors())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getContributors())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("contributors", getContributors())
                .toString();
    }
}