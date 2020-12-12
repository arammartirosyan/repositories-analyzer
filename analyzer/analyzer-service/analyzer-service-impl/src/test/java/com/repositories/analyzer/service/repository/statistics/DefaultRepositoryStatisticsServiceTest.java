package com.repositories.analyzer.service.repository.statistics;

import com.repositories.analyzer.persistence.RepositoryDocument;
import com.repositories.analyzer.persistence.repository.RepositoryDocumentRepository;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommit;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributor;
import com.repositories.analyzer.service.AbstractServiceTest;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import com.repositories.analyzer.service.repository.statistics.commits.RetrieveRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.common.RetrieveStatisticsLastRefreshedOnResult;
import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;
import com.repositories.analyzer.service.repository.statistics.contributors.RetrieveRepositoryContributorsResult;
import com.repositories.analyzer.service.utils.ServiceTestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class DefaultRepositoryStatisticsServiceTest extends AbstractServiceTest {

    @Mock
    private RepositoryDocumentRepository repositoryDocumentRepository;

    @InjectMocks
    private DefaultRepositoryStatisticsService repositoryStatisticsService;

    @After
    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(repositoryDocumentRepository);
    }

    @Test
    public void testRetrieveNotExistingRepositoryCommits() {
        final String repositoryUid = uuid();

        when(repositoryDocumentRepository.findRepositoryCommitsStatistics(repositoryUid)).thenReturn(Optional.empty());

        final RetrieveRepositoryCommitsResult commitsResult = repositoryStatisticsService.retrieveCommits(repositoryUid);
        Assert.assertTrue(commitsResult.failures().isEmpty() && commitsResult.commits().isEmpty());

        verify(repositoryDocumentRepository).findRepositoryCommitsStatistics(repositoryUid);
    }

    @Test
    public void testRetrieveCommits() {
        final String repositoryUid = uuid();
        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocumentWithCommits(repositoryUid);
        final List<RepositoryCommit> commits = repositoryDocument.getStatistics().getCommitsStatistics().getCommits();

        when(repositoryDocumentRepository.findRepositoryCommitsStatistics(repositoryUid)).thenReturn(Optional.of(repositoryDocument));

        final RetrieveRepositoryCommitsResult commitsResult = repositoryStatisticsService.retrieveCommits(repositoryUid);
        Assert.assertFalse(commitsResult.hasFailures());
        Assert.assertEquals(commits.size(), commitsResult.commits().size());

        final ArrayList<RepositoryCommitDetails> details = new ArrayList<>(commitsResult.commits());
        IntStream.range(0, commits.size()).forEach(index -> assertEquals(commits.get(index), details.get(index)));

        verify(repositoryDocumentRepository).findRepositoryCommitsStatistics(repositoryUid);
    }

    @Test
    public void testRetrieveNotExistingRepositoryContributors() {
        final String repositoryUid = uuid();

        when(repositoryDocumentRepository.findRepositoryContributorsStatistics(repositoryUid)).thenReturn(Optional.empty());

        final RetrieveRepositoryContributorsResult contributorsResult = repositoryStatisticsService.retrieveContributors(repositoryUid);
        Assert.assertTrue(contributorsResult.failures().isEmpty() && contributorsResult.contributors().isEmpty());

        verify(repositoryDocumentRepository).findRepositoryContributorsStatistics(repositoryUid);
    }

    @Test
    public void testRetrieveContributors() {
        final String repositoryUid = uuid();
        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocumentWithContributors(repositoryUid);
        final List<RepositoryContributor> contributors = repositoryDocument.getStatistics().getContributorsStatistics().getContributors();

        when(repositoryDocumentRepository.findRepositoryContributorsStatistics(repositoryUid)).thenReturn(Optional.of(repositoryDocument));

        final RetrieveRepositoryContributorsResult contributorsResult = repositoryStatisticsService.retrieveContributors(repositoryUid);
        Assert.assertFalse(contributorsResult.hasFailures());
        Assert.assertEquals(contributors.size(), contributorsResult.contributors().size());

        final ArrayList<RepositoryContributorDetails> details = new ArrayList<>(contributorsResult.contributors());
        IntStream.range(0, contributors.size()).forEach(index -> assertEquals(contributors.get(index), details.get(index)));

        verify(repositoryDocumentRepository).findRepositoryContributorsStatistics(repositoryUid);
    }

    @Test
    public void testRetrieveNotRefreshedCommitsLastRefreshedOn() {
        final String repositoryUid = uuid();

        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocument(repositoryUid, uuid());

        when(repositoryDocumentRepository.findRepositoryCommitsStatisticsLastRefreshedOn(repositoryUid)).thenReturn(Optional.of(repositoryDocument));

        final RetrieveStatisticsLastRefreshedOnResult refreshedOnResult = repositoryStatisticsService.retrieveCommitsLastRefreshedOn(repositoryUid);
        Assert.assertTrue(refreshedOnResult.lastRefreshedOn().isEmpty());

        verify(repositoryDocumentRepository).findRepositoryCommitsStatisticsLastRefreshedOn(repositoryUid);
    }

    @Test
    public void testRetrieveCommitsLastRefreshedOn() {
        final String repositoryUid = uuid();

        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocumentWithCommits(repositoryUid);

        when(repositoryDocumentRepository.findRepositoryCommitsStatisticsLastRefreshedOn(repositoryUid)).thenReturn(Optional.of(repositoryDocument));

        final RetrieveStatisticsLastRefreshedOnResult refreshedOnResult = repositoryStatisticsService.retrieveCommitsLastRefreshedOn(repositoryUid);
        assertValidLastRefreshedOn(repositoryDocument.getStatistics().getCommitsStatistics().getLastRefreshedOn(), refreshedOnResult);

        verify(repositoryDocumentRepository).findRepositoryCommitsStatisticsLastRefreshedOn(repositoryUid);
    }

    @Test
    public void testRetrieveNotRefreshedContributorsLastRefreshedOn() {
        final String repositoryUid = uuid();

        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocument(repositoryUid, uuid());

        when(repositoryDocumentRepository.findRepositoryContributorsStatisticsLastRefreshedOn(repositoryUid)).thenReturn(Optional.of(repositoryDocument));

        final RetrieveStatisticsLastRefreshedOnResult refreshedOnResult = repositoryStatisticsService.retrieveContributorsLastRefreshedOn(repositoryUid);
        Assert.assertTrue(refreshedOnResult.lastRefreshedOn().isEmpty());

        verify(repositoryDocumentRepository).findRepositoryContributorsStatisticsLastRefreshedOn(repositoryUid);
    }

    @Test
    public void testRetrieveContributorsLastRefreshedOn() {
        final String repositoryUid = uuid();

        final RepositoryDocument repositoryDocument = ServiceTestUtils.repositoryDocumentWithContributors(repositoryUid);

        when(repositoryDocumentRepository.findRepositoryContributorsStatisticsLastRefreshedOn(repositoryUid)).thenReturn(Optional.of(repositoryDocument));

        final RetrieveStatisticsLastRefreshedOnResult refreshedOnResult = repositoryStatisticsService.retrieveContributorsLastRefreshedOn(repositoryUid);
        assertValidLastRefreshedOn(repositoryDocument.getStatistics().getContributorsStatistics().getLastRefreshedOn(), refreshedOnResult);

        verify(repositoryDocumentRepository).findRepositoryContributorsStatisticsLastRefreshedOn(repositoryUid);
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

    private static void assertValidLastRefreshedOn(final LocalDateTime lastRefreshedOn, final RetrieveStatisticsLastRefreshedOnResult refreshedOnResult) {
        Assert.assertTrue(
                refreshedOnResult.lastRefreshedOn()
                        .filter(refreshedOn -> refreshedOn.equals(lastRefreshedOn))
                        .isPresent()
        );
    }
}