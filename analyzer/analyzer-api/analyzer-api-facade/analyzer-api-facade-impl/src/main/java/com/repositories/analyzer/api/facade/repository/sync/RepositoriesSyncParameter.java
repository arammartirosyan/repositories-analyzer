package com.repositories.analyzer.api.facade.repository.sync;

import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;

import java.util.Collection;

public interface RepositoriesSyncParameter {

    Collection<RepositoryDocumentCreationParameter> reposToCreate();

    Collection<RepositoryDocumentModificationParameter> reposToModify();

    static RepositoriesSyncParameter of(final Collection<RepositoryDocumentCreationParameter> reposToCreate,
                                        final Collection<RepositoryDocumentModificationParameter> reposToModify) {
        if (reposToCreate == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'reposToCreate'.");
        }
        if (reposToModify == null) {
            throw new IllegalArgumentException("Null collection was passed as an argument for parameter 'reposToModify'.");
        }
        return new ImmutableRepositoriesSyncParameter(reposToCreate, reposToModify);
    }
}