package com.repositories.analyzer.service.repository.statistics;

import com.repositories.analyzer.service.repository.statistics.commits.RetrieveRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.common.RetrieveStatisticsLastRefreshedOnResult;
import com.repositories.analyzer.service.repository.statistics.contributors.RetrieveRepositoryContributorsResult;

public interface RepositoryStatisticsService {

    RetrieveRepositoryCommitsResult retrieveCommits(String repositoryUid);

    RetrieveRepositoryContributorsResult retrieveContributors(String repositoryUid);

    RetrieveStatisticsLastRefreshedOnResult retrieveCommitsLastRefreshedOn(String repositoryUid);

    RetrieveStatisticsLastRefreshedOnResult retrieveContributorsLastRefreshedOn(String repositoryUid);
}