package com.repositories.analyzer.service.repository.creation.bulk;

import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;

import java.util.Collection;

public interface RepositoryDocumentBulkCreationParameter {

    Collection<RepositoryDocumentCreationParameter> repositories();

    static RepositoryDocumentBulkCreationParameter of(final Collection<RepositoryDocumentCreationParameter> repositories) {
        if (repositories == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'repositories'.");
        }
        return new ImmutableRepositoryDocumentBulkCreationParameter(repositories);
    }
}