package com.repositories.analyzer.api.model.repository.commits;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repositories.analyzer.common.api.model.AbstractResponse;
import com.repositories.analyzer.common.usecase.Failure;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

public class RetrieveRepositoryCommitsResponse extends AbstractResponse {

    @JsonProperty
    private Collection<RepositoryCommitsAggregationDto> commits;

    public RetrieveRepositoryCommitsResponse() {
        super();
    }

    public RetrieveRepositoryCommitsResponse(final Collection<? extends Failure> failures) {
        super(failures);
    }

    public Collection<RepositoryCommitsAggregationDto> getCommits() {
        return commits;
    }

    public void setCommits(final Collection<RepositoryCommitsAggregationDto> commits) {
        this.commits = commits;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetrieveRepositoryCommitsResponse)) {
            return false;
        }
        final RetrieveRepositoryCommitsResponse that = (RetrieveRepositoryCommitsResponse) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getCommits(), that.getCommits())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getCommits())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("commits", getCommits())
                .toString();
    }
}