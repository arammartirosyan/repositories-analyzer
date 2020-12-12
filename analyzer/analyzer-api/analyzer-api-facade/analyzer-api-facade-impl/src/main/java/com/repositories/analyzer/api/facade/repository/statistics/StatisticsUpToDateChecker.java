package com.repositories.analyzer.api.facade.repository.statistics;

public interface StatisticsUpToDateChecker {

    boolean commitsStatisticsUpToDate(String repositoryUid);

    boolean contributorsStatisticsUpToDate(String repositoryUid);
}