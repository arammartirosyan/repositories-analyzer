package com.repositories.analyzer.api.model.repository.commits;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class RepositoryCommitsAggregationDto {

    @JsonProperty
    private String name;

    @JsonProperty
    private String email;

    @JsonProperty
    private Integer count;

    @JsonProperty
    private List<RepositoryCommitDto> details;

    public RepositoryCommitsAggregationDto() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    public List<RepositoryCommitDto> getDetails() {
        return details;
    }

    public void setDetails(final List<RepositoryCommitDto> details) {
        this.details = details;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryCommitsAggregationDto)) {
            return false;
        }
        final RepositoryCommitsAggregationDto that = (RepositoryCommitsAggregationDto) o;
        return new EqualsBuilder()
                .append(getName(), that.getName())
                .append(getEmail(), that.getEmail())
                .append(getCount(), that.getCount())
                .append(getDetails(), that.getDetails())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getName())
                .append(getEmail())
                .append(getCount())
                .append(getDetails())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", getName())
                .append("email", getEmail())
                .append("count", getCount())
                .append("details", getDetails())
                .toString();
    }
}