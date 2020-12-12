package com.repositories.analyzer.service.repository.statistics.contributors;

import com.repositories.analyzer.common.usecase.Result;

import java.util.Collection;
import java.util.List;

public interface RetrieveRepositoryContributorsResult extends Result<RetrieveRepositoryContributorsFailure> {

    Collection<RepositoryContributorDetails> contributors();

    static RetrieveRepositoryContributorsResult empty() {
        return new ImmutableRetrieveRepositoryContributorsResult(List.of());
    }

    static RetrieveRepositoryContributorsResult of(final Collection<RepositoryContributorDetails> contributors) {
        if (contributors == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'contributors'.");
        }
        return new ImmutableRetrieveRepositoryContributorsResult(contributors);
    }
}