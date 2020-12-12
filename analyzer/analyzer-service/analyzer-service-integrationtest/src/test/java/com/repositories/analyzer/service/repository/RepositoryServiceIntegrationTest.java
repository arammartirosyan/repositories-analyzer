package com.repositories.analyzer.service.repository;

import com.repositories.analyzer.persistence.RepositoryDocument;
import com.repositories.analyzer.persistence.repository.RepositoryDocumentRepository;
import com.repositories.analyzer.service.AbstractServiceIntegrationTest;
import com.repositories.analyzer.service.repository.common.RepositoryRef;
import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationParameter;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationResult;
import com.repositories.analyzer.service.repository.details.RetrieveRepositoryDetailsResult;
import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationParameter;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationResult;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsResult;
import com.repositories.analyzer.service.utils.ServiceIntegrationTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RepositoryDocumentRepository repositoryDocumentRepository;

    @Test
    public void testRepositoryRefs() {
        final List<RepositoryDocument> repositories = List.of(
                ServiceIntegrationTestUtils.repositoryDocument(), ServiceIntegrationTestUtils.repositoryDocument()
        );
        final List<RepositoryDocument> createdRepositories = repositoryDocumentRepository.insert(repositories);

        final Collection<RepositoryRef> repositoryRefs = repositoryService.repositoryRefs(
                repositories.stream()
                        .map(RepositoryDocument::getRemoteId)
                        .collect(Collectors.toUnmodifiableList())
        );
        Assert.assertEquals(createdRepositories.size(), repositoryRefs.size());
        createdRepositories.forEach(document ->
                Assert.assertTrue(repositoryRefs.contains(RepositoryRef.of(document.getUid(), document.getRemoteId())))
        );
    }

    @Test
    public void testRetrieveDetails() {
        final RepositoryDocument createdDocument = repositoryDocumentRepository.insert(
                ServiceIntegrationTestUtils.repositoryDocument()
        );
        final RetrieveRepositoryDetailsResult detailsResult = repositoryService.retrieveDetails(createdDocument.getUid());
        Assert.assertFalse(detailsResult.hasFailures());
        Assert.assertEquals(createdDocument.getUid(), detailsResult.details().uid());
        Assert.assertEquals(createdDocument.getName(), detailsResult.details().name());
        Assert.assertEquals(createdDocument.getFullName(), detailsResult.details().fullName());
    }

    @Test
    public void testBulkCreate() {
        final List<RepositoryDocumentCreationParameter> creationParameters = List.of(
                ServiceIntegrationTestUtils.creationParameter(), ServiceIntegrationTestUtils.creationParameter()
        );
        final RepositoryDocumentBulkCreationResult creationResult = repositoryService.bulkCreate(
                RepositoryDocumentBulkCreationParameter.of(creationParameters)
        );
        Assert.assertFalse(creationResult.hasFailures());
        Assert.assertEquals(creationParameters.size(), creationResult.repositories().size());
    }

    @Test
    public void testBulkModify() {
        final List<RepositoryDocumentCreationParameter> creationParameters = List.of(
                ServiceIntegrationTestUtils.creationParameter(), ServiceIntegrationTestUtils.creationParameter()
        );
        final RepositoryDocumentBulkCreationResult creationResult = repositoryService.bulkCreate(
                RepositoryDocumentBulkCreationParameter.of(creationParameters)
        );
        final List<RepositoryDocumentModificationParameter> modificationParameters = creationParameters.stream()
                .map(creationParameter -> ServiceIntegrationTestUtils.modificationParameter(creationParameter.remoteId()))
                .collect(Collectors.toList());

        final RepositoryDocumentBulkModificationResult modificationResult = repositoryService.bulkModify(
                RepositoryDocumentBulkModificationParameter.of(modificationParameters)
        );
        Assert.assertFalse(creationResult.hasFailures());
        Assert.assertEquals(creationResult.repositories().size(), modificationResult.repositories().size());
    }

    @Test
    public void testRefreshRepositoryCommits() {
        final RepositoryDocument createdDocument = repositoryDocumentRepository.insert(
                ServiceIntegrationTestUtils.repositoryDocument()
        );
        final List<RefreshRepositoryCommitParameter> refreshParameters = List.of(
                ServiceIntegrationTestUtils.refreshCommitParameter(), ServiceIntegrationTestUtils.refreshCommitParameter()
        );
        final RefreshRepositoryCommitsResult refreshResult = repositoryService.refreshRepositoryCommits(
                RefreshRepositoryCommitsParameter.of(createdDocument.getUid(), refreshParameters)
        );
        Assert.assertFalse(refreshResult.hasFailures());
        Assert.assertEquals(refreshParameters.size(), refreshResult.commits().size());
    }

    @Test
    public void testRefreshRepositoryContributors() {
        final RepositoryDocument createdDocument = repositoryDocumentRepository.insert(
                ServiceIntegrationTestUtils.repositoryDocument()
        );
        final List<RefreshRepositoryContributorParameter> refreshParameters = List.of(
                ServiceIntegrationTestUtils.refreshContributorParameter(), ServiceIntegrationTestUtils.refreshContributorParameter()
        );
        final RefreshRepositoryContributorsResult refreshResult = repositoryService.refreshRepositoryContributors(
                RefreshRepositoryContributorsParameter.of(createdDocument.getUid(), refreshParameters)
        );
        Assert.assertFalse(refreshResult.hasFailures());
        Assert.assertEquals(refreshParameters.size(), refreshResult.contributors().size());
    }
}