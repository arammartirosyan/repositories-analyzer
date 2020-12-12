package com.repositories.analyzer.service.repository.details;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

final class ImmutableRepositoryDetails implements RepositoryDetails {

    private final String uid;

    private final String name;

    private final String fullName;

    ImmutableRepositoryDetails(final String uid, final String name, final String fullName) {
        this.uid = uid;
        this.name = name;
        this.fullName = fullName;
    }

    @Override
    public String uid() {
        return uid;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDetails)) {
            return false;
        }
        final RepositoryDetails that = (RepositoryDetails) o;
        return new EqualsBuilder()
                .append(uid(), that.uid())
                .append(name(), that.name())
                .append(fullName(), that.fullName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(uid())
                .append(name())
                .append(fullName())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uid", uid())
                .append("name", name())
                .append("fullName", fullName())
                .toString();
    }
}