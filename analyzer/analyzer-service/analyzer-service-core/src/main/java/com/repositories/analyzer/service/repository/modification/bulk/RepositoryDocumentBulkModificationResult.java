package com.repositories.analyzer.service.repository.modification.bulk;

import com.repositories.analyzer.common.usecase.Result;
import com.repositories.analyzer.service.repository.details.RepositoryDetails;

import java.util.Collection;

public interface RepositoryDocumentBulkModificationResult extends Result<RepositoryDocumentBulkModificationFailure> {

    Collection<RepositoryDetails> repositories();

    static RepositoryDocumentBulkModificationResult of(final Collection<RepositoryDetails> repositories) {
        if (repositories == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'repositories'.");
        }
        return new ImmutableRepositoryDocumentBulkModificationResult(repositories);
    }
}