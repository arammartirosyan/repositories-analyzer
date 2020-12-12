package com.repositories.analyzer.service.repository.details;

import com.repositories.analyzer.common.usecase.Result;

import java.util.List;

public interface RetrieveRepositoryDetailsResult extends Result<RetrieveRepositoryDetailsFailure> {

    RepositoryDetails details();

    static RetrieveRepositoryDetailsResult repositoryNotFound() {
        return new ImmutableRetrieveRepositoryDetailsResult(
                List.of(RetrieveRepositoryDetailsFailure.REPOSITORY_NOT_FOUND)
        );
    }

    static RetrieveRepositoryDetailsResult of(final RepositoryDetails repositoryDetails) {
        if (repositoryDetails == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'repositoryDetails'.");
        }
        return new ImmutableRetrieveRepositoryDetailsResult(repositoryDetails);
    }
}