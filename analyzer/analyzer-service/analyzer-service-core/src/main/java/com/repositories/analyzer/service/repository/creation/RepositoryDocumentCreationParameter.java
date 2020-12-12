package com.repositories.analyzer.service.repository.creation;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

public interface RepositoryDocumentCreationParameter {

    String remoteId();

    String name();

    String fullName();

    String description();

    String language();

    LocalDateTime createdOn();

    static RepositoryDocumentCreationParameter of(final String remoteId, final String name, final String fullName, final String description, final String language, final LocalDateTime createdOn) {
        if (StringUtils.isBlank(remoteId)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'remoteId'.");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'name'.");
        }
        if (StringUtils.isBlank(fullName)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'fullName'.");
        }
        if (createdOn == null) {
            throw new IllegalArgumentException("Null was passed as an argument for parameter 'createdOn'.");
        }
        return new ImmutableRepositoryDocumentCreationParameter(remoteId, name, fullName, description, language, createdOn);
    }
}