package com.repositories.analyzer.service.repository.creation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

final class ImmutableRepositoryDocumentCreationParameter implements RepositoryDocumentCreationParameter {

    private final String remoteId;

    private final String name;

    private final String fullName;

    private final String description;

    private final String language;

    private final LocalDateTime createdOn;

    ImmutableRepositoryDocumentCreationParameter(
            final String remoteId,
            final String name,
            final String fullName,
            final String description,
            final String language,
            final LocalDateTime createdOn
    ) {
        this.remoteId = remoteId;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.language = language;
        this.createdOn = createdOn;
    }

    @Override
    public String remoteId() {
        return remoteId;
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
    public String description() {
        return description;
    }

    @Override
    public String language() {
        return language;
    }

    @Override
    public LocalDateTime createdOn() {
        return createdOn;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDocumentCreationParameter)) {
            return false;
        }
        final RepositoryDocumentCreationParameter that = (RepositoryDocumentCreationParameter) o;
        return new EqualsBuilder()
                .append(remoteId(), that.remoteId())
                .append(name(), that.name())
                .append(fullName(), that.fullName())
                .append(description(), that.description())
                .append(language(), that.language())
                .append(createdOn(), that.createdOn())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(remoteId())
                .append(name())
                .append(fullName())
                .append(description())
                .append(language())
                .append(createdOn())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("remoteId", remoteId())
                .append("name", name())
                .append("fullName", fullName())
                .append("description", description())
                .append("language", language())
                .append("createdOn", createdOn())
                .toString();
    }
}