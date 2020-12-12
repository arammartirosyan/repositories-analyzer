package com.repositories.analyzer.service.repository.modification;

import org.apache.commons.lang3.StringUtils;

public interface RepositoryDocumentModificationParameter {

    String remoteId();

    String name();

    String fullName();

    String description();

    String language();

    static RepositoryDocumentModificationParameter of(final String remoteId, final String name, final String fullName, final String description, final String language) {
        if (StringUtils.isBlank(remoteId)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'remoteId'.");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'name'.");
        }
        if (StringUtils.isBlank(fullName)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'fullName'.");
        }
        return new ImmutableRepositoryDocumentModificationParameter(remoteId, name, fullName, description, language);
    }
}