package com.repositories.analyzer.service.repository;

import com.repositories.analyzer.service.repository.common.RepositoryRef;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationParameter;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationResult;
import com.repositories.analyzer.service.repository.details.RetrieveRepositoryDetailsResult;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationParameter;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationResult;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsResult;

import java.util.Collection;

public interface RepositoryService {

    Collection<RepositoryRef> repositoryRefs(Collection<String> remoteIds);

    RetrieveRepositoryDetailsResult retrieveDetails(String repositoryUid);

    RepositoryDocumentBulkCreationResult bulkCreate(RepositoryDocumentBulkCreationParameter parameter);

    RepositoryDocumentBulkModificationResult bulkModify(RepositoryDocumentBulkModificationParameter parameter);

    RefreshRepositoryCommitsResult refreshRepositoryCommits(RefreshRepositoryCommitsParameter parameter);

    RefreshRepositoryContributorsResult refreshRepositoryContributors(RefreshRepositoryContributorsParameter parameter);
}