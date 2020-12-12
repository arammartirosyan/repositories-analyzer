package com.repositories.analyzer.service.repository.statistics.commits.refresh;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public interface RefreshRepositoryCommitsParameter {

    String repositoryUid();

    Collection<RefreshRepositoryCommitParameter> commits();

    static RefreshRepositoryCommitsParameter of(final String repositoryUid, final Collection<RefreshRepositoryCommitParameter> commits) {
        if (StringUtils.isBlank(repositoryUid)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'repositoryUid'.");
        }
        if (commits == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'commits'.");
        }
        return new ImmutableRefreshRepositoryCommitsParameter(repositoryUid, commits);
    }
}