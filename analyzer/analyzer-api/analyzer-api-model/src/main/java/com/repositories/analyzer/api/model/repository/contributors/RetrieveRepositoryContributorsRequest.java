package com.repositories.analyzer.api.model.repository.contributors;

import com.repositories.analyzer.common.api.model.AbstractRequest;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RetrieveRepositoryContributorsRequest extends AbstractRequest {

    private final String repositoryUid;

    public RetrieveRepositoryContributorsRequest(final String repositoryUid) {
        this.repositoryUid = repositoryUid;
    }

    public String getRepositoryUid() {
        return repositoryUid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetrieveRepositoryContributorsRequest)) {
            return false;
        }
        final RetrieveRepositoryContributorsRequest that = (RetrieveRepositoryContributorsRequest) o;
        return new EqualsBuilder()
                .append(getRepositoryUid(), that.getRepositoryUid())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getRepositoryUid())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("repositoryUid", getRepositoryUid())
                .toString();
    }
}