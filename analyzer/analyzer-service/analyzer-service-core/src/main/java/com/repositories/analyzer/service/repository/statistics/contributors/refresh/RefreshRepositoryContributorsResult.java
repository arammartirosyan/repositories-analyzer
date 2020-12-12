package com.repositories.analyzer.service.repository.statistics.contributors.refresh;

import com.repositories.analyzer.common.usecase.Result;
import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;

import java.util.Collection;
import java.util.List;

public interface RefreshRepositoryContributorsResult extends Result<RefreshRepositoryContributorsFailure> {

    Collection<RepositoryContributorDetails> contributors();

    static RefreshRepositoryContributorsResult repositoryNotFound() {
        return new ImmutableRefreshRepositoryContributorsResult(
                null, List.of(RefreshRepositoryContributorsFailure.REPOSITORY_NOT_FOUND)
        );
    }

    static RefreshRepositoryContributorsResult of(final Collection<RepositoryContributorDetails> contributors) {
        if (contributors == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'contributors'.");
        }
        return new ImmutableRefreshRepositoryContributorsResult(contributors, List.of());
    }
}