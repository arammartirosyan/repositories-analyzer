package com.repositories.analyzer.service.repository.details;

import org.apache.commons.lang3.StringUtils;

public interface RepositoryDetails {

    String uid();

    String name();

    String fullName();

    static RepositoryDetails of(final String uid, final String name, final String fullName) {
        if (StringUtils.isBlank(uid)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'uid'.");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'name'.");
        }
        if (StringUtils.isBlank(fullName)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'fullName'.");
        }
        return new ImmutableRepositoryDetails(uid, name, fullName);
    }
}