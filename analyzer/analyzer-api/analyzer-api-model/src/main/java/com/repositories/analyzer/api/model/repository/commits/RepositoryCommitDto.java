package com.repositories.analyzer.api.model.repository.commits;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

public class RepositoryCommitDto {

    @JsonProperty
    private String message;

    @JsonProperty
    private LocalDate committedOn;

    public RepositoryCommitDto() {
        super();
    }

    public RepositoryCommitDto(final String message, final LocalDate committedOn) {
        this.message = message;
        this.committedOn = committedOn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public LocalDate getCommittedOn() {
        return committedOn;
    }

    public void setCommittedOn(final LocalDate committedOn) {
        this.committedOn = committedOn;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryCommitDto)) {
            return false;
        }
        final RepositoryCommitDto that = (RepositoryCommitDto) o;
        return new EqualsBuilder()
                .append(getMessage(), that.getMessage())
                .append(getCommittedOn(), that.getCommittedOn())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getMessage())
                .append(getCommittedOn())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("message", getMessage())
                .append("committedOn", getCommittedOn())
                .toString();
    }
}