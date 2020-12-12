package com.repositories.analyzer.api.model.repository.contributors;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RepositoryContributorDto {

    @JsonProperty
    private String remoteId;

    @JsonProperty
    private String login;

    @JsonProperty
    private String type;

    public RepositoryContributorDto() {
        super();
    }

    public RepositoryContributorDto(final String remoteId, final String login, final String type) {
        this.remoteId = remoteId;
        this.login = login;
        this.type = type;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(final String remoteId) {
        this.remoteId = remoteId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryContributorDto)) {
            return false;
        }
        final RepositoryContributorDto that = (RepositoryContributorDto) o;
        return new EqualsBuilder()
                .append(getRemoteId(), that.getRemoteId())
                .append(getLogin(), that.getLogin())
                .append(getType(), that.getType())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getRemoteId())
                .append(getLogin())
                .append(getType())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("remoteId", getRemoteId())
                .append("login", getLogin())
                .append("type", getType())
                .toString();
    }
}