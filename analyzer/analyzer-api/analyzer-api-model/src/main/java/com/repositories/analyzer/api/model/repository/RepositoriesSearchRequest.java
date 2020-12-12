package com.repositories.analyzer.api.model.repository;

import com.repositories.analyzer.common.api.model.AbstractRequest;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RepositoriesSearchRequest extends AbstractRequest {

    private final String query;

    public RepositoriesSearchRequest(final String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoriesSearchRequest)) {
            return false;
        }
        final RepositoriesSearchRequest that = (RepositoriesSearchRequest) o;
        return new EqualsBuilder()
                .append(getQuery(), that.getQuery())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getQuery())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("query", getQuery())
                .toString();
    }
}