package com.repositories.analyzer.api.model.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RepositoryDto {

    @JsonProperty
    private String uid;

    @JsonProperty
    private String name;

    @JsonProperty
    private String fullName;

    public RepositoryDto() {
        super();
    }

    public RepositoryDto(final String uid, final String name, final String fullName) {
        this.uid = uid;
        this.name = name;
        this.fullName = fullName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDto)) {
            return false;
        }
        final RepositoryDto that = (RepositoryDto) o;
        return new EqualsBuilder()
                .append(getUid(), that.getUid())
                .append(getName(), that.getName())
                .append(getFullName(), that.getFullName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getUid())
                .append(getName())
                .append(getFullName())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uid", getUid())
                .append("name", getName())
                .append("fullName", getFullName())
                .toString();
    }
}