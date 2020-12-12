package com.repositories.analyzer.service.repository.modification.bulk;

import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;

import java.util.Collection;

public interface RepositoryDocumentBulkModificationParameter {

    Collection<RepositoryDocumentModificationParameter> repositories();

    static RepositoryDocumentBulkModificationParameter of(final Collection<RepositoryDocumentModificationParameter> repositories) {
        if (repositories == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'repositories'.");
        }
        return new ImmutableRepositoryDocumentBulkModificationParameter(repositories);
    }
}