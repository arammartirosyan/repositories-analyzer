package com.repositories.analyzer.service.repository.creation.bulk;

import com.repositories.analyzer.common.usecase.Result;
import com.repositories.analyzer.service.repository.details.RepositoryDetails;

import java.util.Collection;

public interface RepositoryDocumentBulkCreationResult extends Result<RepositoryDocumentBulkCreationFailure> {

    Collection<RepositoryDetails> repositories();

    static RepositoryDocumentBulkCreationResult of(final Collection<RepositoryDetails> repositories) {
        if (repositories == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'repositories'.");
        }
        return new ImmutableRepositoryDocumentBulkCreationResult(repositories);
    }
}