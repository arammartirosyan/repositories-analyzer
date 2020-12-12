package com.repositories.analyzer.service.repository.statistics.commits.refresh;

import com.repositories.analyzer.common.usecase.Result;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;

import java.util.Collection;
import java.util.List;

public interface RefreshRepositoryCommitsResult extends Result<RefreshRepositoryCommitsFailure> {

    Collection<RepositoryCommitDetails> commits();

    static RefreshRepositoryCommitsResult repositoryNotFound() {
        return new ImmutableRefreshRepositoryCommitsResult(
                null, List.of(RefreshRepositoryCommitsFailure.REPOSITORY_NOT_FOUND)
        );
    }

    static RefreshRepositoryCommitsResult of(final Collection<RepositoryCommitDetails> commits) {
        if (commits == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'commits'.");
        }
        return new ImmutableRefreshRepositoryCommitsResult(commits, List.of());
    }
}