package com.repositories.analyzer.service.repository.common;

import org.apache.commons.lang3.StringUtils;

public interface RepositoryRef {

    String uid();

    String remoteId();

    static RepositoryRef of(final String uid, final String remoteId) {
        if (StringUtils.isBlank(uid)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'uid'.");
        }
        if (StringUtils.isBlank(remoteId)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'remoteId'.");
        }
        return new ImmutableRepositoryRef(uid, remoteId);
    }
}