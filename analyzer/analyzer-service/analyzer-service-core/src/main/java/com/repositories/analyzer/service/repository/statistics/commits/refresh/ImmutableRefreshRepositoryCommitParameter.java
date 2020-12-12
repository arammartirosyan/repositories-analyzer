package com.repositories.analyzer.service.repository.statistics.commits.refresh;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

final class ImmutableRefreshRepositoryCommitParameter implements RefreshRepositoryCommitParameter {

    private final String name;

    private final String email;

    private final String message;

    private final LocalDate committedOn;

    ImmutableRefreshRepositoryCommitParameter(final String name, final String email, final String message, final LocalDate committedOn) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.committedOn = committedOn;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public LocalDate committedOn() {
        return committedOn;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefreshRepositoryCommitParameter)) {
            return false;
        }
        final RefreshRepositoryCommitParameter that = (RefreshRepositoryCommitParameter) o;
        return new EqualsBuilder()
                .append(name(), that.name())
                .append(email(), that.email())
                .append(message(), that.message())
                .append(committedOn(), that.committedOn())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name())
                .append(email())
                .append(message())
                .append(committedOn())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name())
                .append("email", email())
                .append("message", message())
                .append("committedOn", committedOn())
                .toString();
    }
}