package com.repositories.analyzer.service.repository.statistics.contributors;

import org.apache.commons.lang3.StringUtils;

public interface RepositoryContributorDetails {

    String remoteId();

    String login();

    String type();

    static RepositoryContributorDetails of(final String remoteId, final String login, final String type) {
        if (StringUtils.isBlank(remoteId)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'remoteId'.");
        }
        if (StringUtils.isBlank(login)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'login'.");
        }
        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'type'.");
        }
        return new ImmutableRepositoryContributorDetails(remoteId, login, type);
    }
}