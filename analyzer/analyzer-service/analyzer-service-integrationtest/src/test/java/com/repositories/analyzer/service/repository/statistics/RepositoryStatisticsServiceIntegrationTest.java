package com.repositories.analyzer.service.repository.statistics;

import com.repositories.analyzer.persistence.RepositoryDocument;
import com.repositories.analyzer.persistence.repository.RepositoryDocumentRepository;
import com.repositories.analyzer.service.AbstractServiceIntegrationTest;
import com.repositories.analyzer.service.repository.statistics.commits.RetrieveRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.common.RetrieveStatisticsLastRefreshedOnResult;
import com.repositories.analyzer.service.repository.statistics.contributors.RetrieveRepositoryContributorsResult;
import com.repositories.analyzer.service.utils.ServiceIntegrationTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class RepositoryStatisticsServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private RepositoryStatisticsService repositoryStatisticsService;

    @Autowired
    private RepositoryDocumentRepository repositoryDocumentRepository;

    @Test
    public void testRetrieveCommits() {
        final RepositoryDocument repositoryDocument = repositoryDocumentRepository.insert(
                ServiceIntegrationTestUtils.repositoryDocumentWithCommits()
        );
        final RetrieveRepositoryCommitsResult commitsResult = repositoryStatisticsService.retrieveCommits(
                repositoryDocument.getUid()
        );
        Assert.assertFalse(commitsResult.hasFailures());
        Assert.assertFalse(commitsResult.commits().isEmpty());
    }

    @Test
    public void testRetrieveContributors() {
        final RepositoryDocument repositoryDocument = repositoryDocumentRepository.insert(
                ServiceIntegrationTestUtils.repositoryDocumentWithContributors()
        );
        final RetrieveRepositoryContributorsResult contributorsResult = repositoryStatisticsService.retrieveContributors(
                repositoryDocument.getUid()
        );
        Assert.assertFalse(contributorsResult.hasFailures());
        Assert.assertFalse(contributorsResult.contributors().isEmpty());
    }

    @Test
    public void testRetrieveCommitsLastRefreshedOn() {
        final RepositoryDocument repositoryDocument = repositoryDocumentRepository.insert(
                ServiceIntegrationTestUtils.repositoryDocumentWithCommits()
        );
        final RetrieveStatisticsLastRefreshedOnResult refreshedOnResult =
                repositoryStatisticsService.retrieveCommitsLastRefreshedOn(repositoryDocument.getUid());

        Assert.assertTrue(refreshedOnResult.lastRefreshedOn().isPresent());
        assertValidRefreshedOn(
                repositoryDocument.getStatistics().getCommitsStatistics().getLastRefreshedOn(),
                refreshedOnResult.lastRefreshedOn().get()
        );
    }

    @Test
    public void testRetrieveContributorsLastRefreshedOn() {
        final RepositoryDocument repositoryDocument = repositoryDocumentRepository.insert(
                ServiceIntegrationTestUtils.repositoryDocumentWithContributors()
        );
        final RetrieveStatisticsLastRefreshedOnResult refreshedOnResult =
                repositoryStatisticsService.retrieveContributorsLastRefreshedOn(repositoryDocument.getUid());

        Assert.assertTrue(refreshedOnResult.lastRefreshedOn().isPresent());
        assertValidRefreshedOn(
                repositoryDocument.getStatistics().getContributorsStatistics().getLastRefreshedOn(),
                refreshedOnResult.lastRefreshedOn().get()
        );
    }

    private static void assertValidRefreshedOn(final LocalDateTime first, final LocalDateTime second) {
        Assert.assertTrue(ServiceIntegrationTestUtils.isEqual(first, second));
    }
}