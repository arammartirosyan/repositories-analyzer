package com.repositories.analyzer.service.repository.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

final class ImmutableRepositoryRef implements RepositoryRef {

    private final String uid;

    private final String remoteId;

    ImmutableRepositoryRef(final String uid, final String remoteId) {
        this.uid = uid;
        this.remoteId = remoteId;
    }

    @Override
    public String uid() {
        return uid;
    }

    @Override
    public String remoteId() {
        return remoteId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryRef)) {
            return false;
        }
        final RepositoryRef that = (RepositoryRef) o;
        return new EqualsBuilder()
                .append(uid(), that.uid())
                .append(remoteId(), that.remoteId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(uid())
                .append(remoteId())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uid", uid())
                .append("remoteId", remoteId())
                .toString();
    }
}