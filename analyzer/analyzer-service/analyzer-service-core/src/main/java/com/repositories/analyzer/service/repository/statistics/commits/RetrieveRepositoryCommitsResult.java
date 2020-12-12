package com.repositories.analyzer.service.repository.statistics.commits;

import com.repositories.analyzer.common.usecase.Result;

import java.util.Collection;
import java.util.List;

public interface RetrieveRepositoryCommitsResult extends Result<RetrieveRepositoryCommitsFailure> {

    Collection<RepositoryCommitDetails> commits();

    static RetrieveRepositoryCommitsResult empty() {
        return new ImmutableRetrieveRepositoryCommitsResult(List.of());
    }

    static RetrieveRepositoryCommitsResult of(final Collection<RepositoryCommitDetails> commits) {
        if (commits == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'commits'.");
        }
        return new ImmutableRetrieveRepositoryCommitsResult(commits);
    }
}