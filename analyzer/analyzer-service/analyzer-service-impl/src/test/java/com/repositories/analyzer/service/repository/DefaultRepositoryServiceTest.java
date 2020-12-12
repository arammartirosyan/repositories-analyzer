package com.repositories.analyzer.service.repository;

import com.repositories.analyzer.persistence.RepositoryDocument;
import com.repositories.analyzer.persistence.repository.RepositoryDocumentRepository;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommit;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributor;
import com.repositories.analyzer.service.AbstractServiceTest;
import com.repositories.analyzer.service.repository.common.RepositoryRef;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationParameter;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationResult;
import com.repositories.analyzer.service.repository.details.RepositoryDetails;
import com.repositories.analyzer.service.repository.details.RetrieveRepositoryDetailsFailure;
import com.repositories.analyzer.service.repository.details.RetrieveRepositoryDetailsResult;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationParameter;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationResult;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsFailure;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsFailure;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsResult;
import com.repositories.analyzer.service.utils.ServiceTestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class DefaultRepositoryServiceTest extends AbstractServiceTest {

    @Mock
    private RepositoryDocumentRepository repositoryDocumentRepository;

    @InjectMocks
    private DefaultRepositoryService repositoryService;

    @After
    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(repositoryDocumentRepository);
    }

    @Test
    public void testEmptyRepositoryRefs() {
        final String remoteId = UUID.randomUUID().toString();

        final List<String> remoteIds = List.of(remoteId);

        when(repositoryDocumentRepository.findAllRepositoryRefsByRemoteIdIn(remoteIds)).thenReturn(List.of());

        final Collection<RepositoryRef> repositoryRefs = repositoryService.repositoryRefs(remoteIds);
        Assert.assertTrue(repositoryRefs.isEmpty());

        verify(repositoryDocumentRepository).findAllRepositoryRefsByRemoteIdIn(remoteIds);
    }

    @Test
    public void testRepositoryRefs() {
        final String firstUid = UUID.randomUUID().toString();
        final String firstRemoteId = UUID.randomUUID().toString();
        final RepositoryDocument firstDocument = ServiceTestUtils.repositoryDocument(firstUid, firstRemoteId);

        final String secondUid = UUID.randomUUID().toString();
        final String secondRemoteId = UUID.randomUUID().toString();
        final RepositoryDocument secondDocument = ServiceTestUtils.repositoryDocument(secondUid, secondRemoteId);

        final List<String> remoteIds = List.of(firstRemoteId, secondRemoteId);

        final List<RepositoryDocument> repositoryDocuments = List.of(firstDocument, secondDocument);

        when(repositoryDocumentRepository.findAllRepositoryRefsByRemoteIdIn(remoteIds)).thenReturn(repositoryDocuments);

        final Collection<RepositoryRef> repositoryRefs = repositoryService.repositoryRefs(remoteIds);
        Assert.assertEquals(repositoryDocuments.size(), repositoryRefs.size());

        final List<RepositoryRef> repositoryRefsAsList = new ArrayList<>(repositoryRefs);
        IntStream.range(0, repositoryRefs.size()).forEach(index ->
                assertEquals(repositoryDocuments.get(index), repositoryRefsAsList.get(index)));

        verify(repositoryDocumentRepository).findAllRepositoryRefsByRemoteIdIn(remoteIds);
    }

    @Test
    public void testRetrieveNotExistingRepositoryDetails() {
        final String repositoryUid = uuid();

        when(repositoryDocumentRepository.findRepositoryDetails(repositoryUid)).thenReturn(Optional.empty());

        final RetrieveRepositoryDetailsResult detailsResult = repositoryService.retrieveDetails(repositoryUid);
        Assert.assertTrue(detailsResult.hasFailures());
        Assert.assertEquals(List.of(RetrieveRepositoryDetailsFailure.REPOSITORY_NOT_FOUND), detailsResult.failures());

        verify(repositoryDocumentRepository).findRepositoryDetails(repositoryUid);
    }

    @Test
    public void testRetrieveDetails() {
        final String repositoryUid = uuid();

        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocument(repositoryUid, uuid());

        when(repositoryDocumentRepository.findRepositoryDetails(repositoryUid)).thenReturn(Optional.of(repositoryDocument));

        final RetrieveRepositoryDetailsResult detailsResult = repositoryService.retrieveDetails(repositoryUid);
        Assert.assertFalse(detailsResult.hasFailures());
        assertEquals(repositoryDocument, detailsResult.details());

        verify(repositoryDocumentRepository).findRepositoryDetails(repositoryUid);
    }

    @Test
    public void testEmptyBulkCreate() {
        final RepositoryDocumentBulkCreationResult creationResult = repositoryService.bulkCreate(
                RepositoryDocumentBulkCreationParameter.of(List.of())
        );
        Assert.assertTrue(creationResult.failures().isEmpty() && creationResult.repositories().isEmpty());
    }

    @Test
    public void testBulkCreate() {
        final List<RepositoryDocument> documents = List.of(
                ServiceTestUtils.repositoryDocument(), ServiceTestUtils.repositoryDocument()
        );
        when(repositoryDocumentRepository.insert(anyCollection())).thenReturn(documents);

        final RepositoryDocumentBulkCreationResult bulkCreationResult = repositoryService.bulkCreate(
                RepositoryDocumentBulkCreationParameter.of(
                        List.of(
                                ServiceTestUtils.repositoryDocumentCreationParameter(),
                                ServiceTestUtils.repositoryDocumentCreationParameter()
                        )
                )
        );
        Assert.assertFalse(bulkCreationResult.hasFailures());
        Assert.assertEquals(documents.size(), bulkCreationResult.repositories().size());

        final List<RepositoryDetails> repositories = new ArrayList<>(bulkCreationResult.repositories());
        IntStream.range(0, documents.size()).forEach(index -> assertEquals(documents.get(index), repositories.get(index)));

        verify(repositoryDocumentRepository).insert(anyCollection());
    }

    @Test
    public void testEmptyBulkModify() {
        final RepositoryDocumentBulkModificationResult modificationResult = repositoryService.bulkModify(
                RepositoryDocumentBulkModificationParameter.of(List.of())
        );
        Assert.assertTrue(modificationResult.failures().isEmpty() && modificationResult.repositories().isEmpty());
    }

    @Test
    public void testBulkModify() {
        final String firstRemoteId = uuid();
        final String secondRemoteId = uuid();
        final List<RepositoryDocument> documents = List.of(
                ServiceTestUtils.repositoryDocument(uuid(), firstRemoteId),
                ServiceTestUtils.repositoryDocument(uuid(), secondRemoteId)
        );
        when(repositoryDocumentRepository.findAllByRemoteIdIn(anyCollection())).thenReturn(documents);
        when(repositoryDocumentRepository.saveAll(anyCollection())).thenReturn(documents);

        final RepositoryDocumentBulkModificationResult modificationResult = repositoryService.bulkModify(
                RepositoryDocumentBulkModificationParameter.of(
                        List.of(
                                ServiceTestUtils.repositoryDocumentModificationParameter(firstRemoteId),
                                ServiceTestUtils.repositoryDocumentModificationParameter(secondRemoteId)
                        )
                )
        );
        Assert.assertFalse(modificationResult.hasFailures());
        Assert.assertEquals(documents.size(), modificationResult.repositories().size());

        final List<RepositoryDetails> repositories = new ArrayList<>(modificationResult.repositories());
        IntStream.range(0, documents.size()).forEach(index -> assertEquals(documents.get(index), repositories.get(index)));

        verify(repositoryDocumentRepository).findAllByRemoteIdIn(anyCollection());
        verify(repositoryDocumentRepository).saveAll(anyCollection());
    }

    @Test
    public void testRefreshNotExistingRepositoryCommits() {
        final String repositoryUid = uuid();

        when(repositoryDocumentRepository.findById(repositoryUid)).thenReturn(Optional.empty());

        final RefreshRepositoryCommitsResult refreshResult = repositoryService.refreshRepositoryCommits(
                RefreshRepositoryCommitsParameter.of(repositoryUid, List.of())
        );
        Assert.assertTrue(refreshResult.hasFailures());
        Assert.assertEquals(List.of(RefreshRepositoryCommitsFailure.REPOSITORY_NOT_FOUND), refreshResult.failures());

        verify(repositoryDocumentRepository).findById(repositoryUid);
    }

    @Test
    public void testRefreshRepositoryCommits() {
        final String repositoryUid = uuid();
        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocumentWithCommits(repositoryUid);
        final List<RepositoryCommit> commits = repositoryDocument.getStatistics().getCommitsStatistics().getCommits();

        when(repositoryDocumentRepository.findById(repositoryUid)).thenReturn(Optional.of(ServiceTestUtils.repositoryDocument()));
        when(repositoryDocumentRepository.save(any(RepositoryDocument.class))).thenReturn(repositoryDocument);

        final RefreshRepositoryCommitsResult refreshResult = repositoryService.refreshRepositoryCommits(
                RefreshRepositoryCommitsParameter.of(
                        repositoryUid,
                        List.of(
                                ServiceTestUtils.refreshRepositoryCommitParameter(),
                                ServiceTestUtils.refreshRepositoryCommitParameter()
                        )
                )
        );
        Assert.assertFalse(refreshResult.hasFailures());
        Assert.assertEquals(commits.size(), refreshResult.commits().size());

        final List<RepositoryCommitDetails> details = new ArrayList<>(refreshResult.commits());
        IntStream.range(0, commits.size()).forEach(index -> assertEquals(commits.get(index), details.get(index)));

        verify(repositoryDocumentRepository).findById(repositoryUid);
        verify(repositoryDocumentRepository).save(any(RepositoryDocument.class));
    }

    @Test
    public void testRefreshNotExistingRepositoryContributors() {
        final String repositoryUid = uuid();

        when(repositoryDocumentRepository.findById(repositoryUid)).thenReturn(Optional.empty());

        final RefreshRepositoryContributorsResult refreshResult = repositoryService.refreshRepositoryContributors(
                RefreshRepositoryContributorsParameter.of(repositoryUid, List.of())
        );
        Assert.assertTrue(refreshResult.hasFailures());
        Assert.assertEquals(List.of(RefreshRepositoryContributorsFailure.REPOSITORY_NOT_FOUND), refreshResult.failures());

        verify(repositoryDocumentRepository).findById(repositoryUid);
    }

    @Test
    public void testRefreshRepositoryContributors() {
        final String repositoryUid = uuid();
        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocumentWithContributors(repositoryUid);
        final List<RepositoryContributor> contributors = repositoryDocument.getStatistics().getContributorsStatistics().getContributors();

        when(repositoryDocumentRepository.findById(repositoryUid)).thenReturn(Optional.of(ServiceTestUtils.repositoryDocument()));
        when(repositoryDocumentRepository.save(any(RepositoryDocument.class))).thenReturn(repositoryDocument);

        final RefreshRepositoryContributorsResult refreshResult = repositoryService.refreshRepositoryContributors(
                RefreshRepositoryContributorsParameter.of(
                        repositoryUid,
                        List.of(
                                ServiceTestUtils.refreshRepositoryContributorParameter(),
                                ServiceTestUtils.refreshRepositoryContributorParameter()
                        )
                )
        );
        Assert.assertFalse(refreshResult.hasFailures());
        Assert.assertEquals(contributors.size(), refreshResult.contributors().size());

        final List<RepositoryContributorDetails> details = new ArrayList<>(refreshResult.contributors());
        IntStream.range(0, contributors.size()).forEach(index -> assertEquals(contributors.get(index), details.get(index)));

        verify(repositoryDocumentRepository).findById(repositoryUid);
        verify(repositoryDocumentRepository).save(any(RepositoryDocument.class));
    }

    private void assertEquals(final RepositoryDocument repositoryDocument, final RepositoryRef repositoryRef) {
        Assert.assertEquals(repositoryDocument.getUid(), repositoryRef.uid());
        Assert.assertEquals(repositoryDocument.getRemoteId(), repositoryRef.remoteId());
    }

    private static void assertEquals(final RepositoryDocument repositoryDocument, final RepositoryDetails repositoryDetails) {
        Assert.assertEquals(repositoryDocument.getUid(), repositoryDetails.uid());
        Assert.assertEquals(repositoryDocument.getName(), repositoryDetails.name());
        Assert.assertEquals(repositoryDocument.getFullName(), repositoryDetails.fullName());
    }

    private static void assertEquals(final RepositoryCommit repositoryCommit, final RepositoryCommitDetails commitDetails) {
        Assert.assertEquals(repositoryCommit.getName(), commitDetails.name());
        Assert.assertEquals(repositoryCommit.getEmail(), commitDetails.email());
        Assert.assertEquals(repositoryCommit.getMessage(), commitDetails.message());
        Assert.assertEquals(repositoryCommit.getCommittedOn(), commitDetails.committedOn());
    }

    private static void assertEquals(final RepositoryContributor repositoryContributor, final RepositoryContributorDetails contributorDetails) {
        Assert.assertEquals(repositoryContributor.getRemoteId(), contributorDetails.remoteId());
        Assert.assertEquals(repositoryContributor.getType(), contributorDetails.type());
        Assert.assertEquals(repositoryContributor.getLogin(), contributorDetails.login());
    }
}