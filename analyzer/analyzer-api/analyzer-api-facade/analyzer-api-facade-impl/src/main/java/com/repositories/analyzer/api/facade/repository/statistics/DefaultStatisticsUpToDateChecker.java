package com.repositories.analyzer.api.facade.repository.statistics;

import com.repositories.analyzer.service.repository.statistics.RepositoryStatisticsService;
import com.repositories.analyzer.service.repository.statistics.common.RetrieveStatisticsLastRefreshedOnResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.function.Function;

@Component
class DefaultStatisticsUpToDateChecker implements StatisticsUpToDateChecker {

    private static final Logger logger = LoggerFactory.getLogger(DefaultStatisticsUpToDateChecker.class);

    private final long commitsStatisticsValidityInSeconds;

    private final long contributorsStatisticsValidityInSeconds;

    private final RepositoryStatisticsService repositoryStatisticsService;

    public DefaultStatisticsUpToDateChecker(
            final RepositoryStatisticsService repositoryStatisticsService,
            @Value("${commits.statistics.validity.in.seconds:300}") final long commitsStatisticsValidityInSeconds,
            @Value("${contributors.statistics.validity.in.seconds:300}") final long contributorsStatisticsValidityInSeconds
    ) {
        this.repositoryStatisticsService = repositoryStatisticsService;
        this.commitsStatisticsValidityInSeconds = commitsStatisticsValidityInSeconds;
        this.contributorsStatisticsValidityInSeconds = contributorsStatisticsValidityInSeconds;
    }

    @Override
    public boolean commitsStatisticsUpToDate(final String repositoryUid) {
        logger.debug("Checking if commits statistics up to date for given repository uid: {}.", repositoryUid);
        return statisticsUpToDate(
                repositoryUid,
                commitsStatisticsValidityInSeconds,
                repositoryStatisticsService::retrieveCommitsLastRefreshedOn
        );
    }

    @Override
    public boolean contributorsStatisticsUpToDate(final String repositoryUid) {
        logger.debug("Checking if contributors statistics up to date for given repository uid: {}.", repositoryUid);
        return statisticsUpToDate(
                repositoryUid,
                contributorsStatisticsValidityInSeconds,
                repositoryStatisticsService::retrieveContributorsLastRefreshedOn
        );
    }

    private boolean statisticsUpToDate(final String repositoryUid, final Long statisticsValidity,
                                       final Function<String, RetrieveStatisticsLastRefreshedOnResult> function) {
        Assert.hasText(repositoryUid, "Null or empty text was passed as an argument for parameter 'repositoryUid'.");
        return function.apply(repositoryUid)
                .lastRefreshedOn()
                .filter(refreshedOn -> refreshedOn.plusSeconds(statisticsValidity).isAfter(LocalDateTime.now()))
                .isPresent();
    }
}