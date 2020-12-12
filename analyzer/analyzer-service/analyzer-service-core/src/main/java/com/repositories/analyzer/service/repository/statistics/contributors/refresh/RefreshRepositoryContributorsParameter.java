package com.repositories.analyzer.service.repository.statistics.contributors.refresh;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public interface RefreshRepositoryContributorsParameter {

    String repositoryUid();

    Collection<RefreshRepositoryContributorParameter> contributors();

    static RefreshRepositoryContributorsParameter of(final String repositoryUid, final Collection<RefreshRepositoryContributorParameter> contributors) {
        if (StringUtils.isBlank(repositoryUid)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'repositoryUid'.");
        }
        if (contributors == null) {
            throw new IllegalArgumentException("Null was passed as an argument for parameter 'remoteId'.");
        }
        return new ImmutableRefreshRepositoryContributorsParameter(repositoryUid, contributors);
    }
}