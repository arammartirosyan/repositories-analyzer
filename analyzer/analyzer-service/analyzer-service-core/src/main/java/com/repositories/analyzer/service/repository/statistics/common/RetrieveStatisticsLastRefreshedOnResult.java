package com.repositories.analyzer.service.repository.statistics.common;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RetrieveStatisticsLastRefreshedOnResult {

    Optional<LocalDateTime> lastRefreshedOn();

    static RetrieveStatisticsLastRefreshedOnResult notRefreshed() {
        return new ImmutableRetrieveStatisticsLastRefreshedOnResult();
    }

    static RetrieveStatisticsLastRefreshedOnResult of(final LocalDateTime lastRefreshedOn) {
        if (lastRefreshedOn == null) {
            throw new IllegalArgumentException("Null was passed as an argument for parameter 'lastRefreshedOn'.");
        }
        return new ImmutableRetrieveStatisticsLastRefreshedOnResult(lastRefreshedOn);
    }
}