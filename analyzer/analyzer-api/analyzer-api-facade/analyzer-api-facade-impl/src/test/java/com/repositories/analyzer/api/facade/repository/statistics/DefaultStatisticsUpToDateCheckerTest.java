package com.repositories.analyzer.api.facade.repository.statistics;

import com.repositories.analyzer.api.facade.AbstractServiceFacadeTest;
import com.repositories.analyzer.service.repository.statistics.RepositoryStatisticsService;
import com.repositories.analyzer.service.repository.statistics.common.RetrieveStatisticsLastRefreshedOnResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class DefaultStatisticsUpToDateCheckerTest extends AbstractServiceFacadeTest {

    private static final long COMMITS_STATISTICS_VALIDITY_IN_SECONDS = 300;

    private static final long CONTRIBUTORS_STATISTICS_VALIDITY_IN_SECONDS = 300;

    @Mock
    private RepositoryStatisticsService repositoryStatisticsService;

    private StatisticsUpToDateChecker statisticsUpToDateChecker;

    @Before
    public void setUp() {
        statisticsUpToDateChecker = new DefaultStatisticsUpToDateChecker(
                repositoryStatisticsService,
                COMMITS_STATISTICS_VALIDITY_IN_SECONDS,
                CONTRIBUTORS_STATISTICS_VALIDITY_IN_SECONDS
        );
    }

    @After
    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(repositoryStatisticsService);
    }

    @Test
    public void testCommitsStatisticsIsNotUpToDate() {
        final String repositoryUid = uuid();
        final LocalDateTime lastRefreshDate = LocalDateTime.now().minusDays(1);

        when(repositoryStatisticsService.retrieveCommitsLastRefreshedOn(repositoryUid))
                .thenReturn(RetrieveStatisticsLastRefreshedOnResult.of(lastRefreshDate));

        final boolean upToDate = statisticsUpToDateChecker.commitsStatisticsUpToDate(repositoryUid);
        Assert.assertFalse(upToDate);

        verify(repositoryStatisticsService).retrieveCommitsLastRefreshedOn(repositoryUid);
    }

    @Test
    public void testCommitsStatisticsIsUpToDate() {
        final String repositoryUid = uuid();
        final LocalDateTime lastRefreshDate = LocalDateTime.now().minusMinutes(1);

        when(repositoryStatisticsService.retrieveCommitsLastRefreshedOn(repositoryUid))
                .thenReturn(RetrieveStatisticsLastRefreshedOnResult.of(lastRefreshDate));

        final boolean upToDate = statisticsUpToDateChecker.commitsStatisticsUpToDate(repositoryUid);
        Assert.assertTrue(upToDate);

        verify(repositoryStatisticsService).retrieveCommitsLastRefreshedOn(repositoryUid);
    }

    @Test
    public void testContributorsStatisticsIsNotUpToDate() {
        final String repositoryUid = uuid();
        final LocalDateTime lastRefreshDate = LocalDateTime.now().minusDays(1);

        when(repositoryStatisticsService.retrieveContributorsLastRefreshedOn(repositoryUid))
                .thenReturn(RetrieveStatisticsLastRefreshedOnResult.of(lastRefreshDate));

        final boolean upToDate = statisticsUpToDateChecker.contributorsStatisticsUpToDate(repositoryUid);
        Assert.assertFalse(upToDate);

        verify(repositoryStatisticsService).retrieveContributorsLastRefreshedOn(repositoryUid);
    }

    @Test
    public void testContributorsStatisticsIsUpToDate() {
        final String repositoryUid = uuid();
        final LocalDateTime lastRefreshDate = LocalDateTime.now().minusMinutes(1);

        when(repositoryStatisticsService.retrieveContributorsLastRefreshedOn(repositoryUid))
                .thenReturn(RetrieveStatisticsLastRefreshedOnResult.of(lastRefreshDate));

        final boolean upToDate = statisticsUpToDateChecker.contributorsStatisticsUpToDate(repositoryUid);
        Assert.assertTrue(upToDate);

        verify(repositoryStatisticsService).retrieveContributorsLastRefreshedOn(repositoryUid);
    }
}