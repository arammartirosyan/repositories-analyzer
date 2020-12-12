package com.repositories.analyzer.service.repository.statistics.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

final class ImmutableRetrieveStatisticsLastRefreshedOnResult implements RetrieveStatisticsLastRefreshedOnResult {

    private final LocalDateTime lastRefreshedOn;

    ImmutableRetrieveStatisticsLastRefreshedOnResult() {
        this.lastRefreshedOn = null;
    }

    ImmutableRetrieveStatisticsLastRefreshedOnResult(final LocalDateTime lastRefreshedOn) {
        this.lastRefreshedOn = lastRefreshedOn;
    }

    @Override
    public Optional<LocalDateTime> lastRefreshedOn() {
        return Optional.ofNullable(lastRefreshedOn);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetrieveStatisticsLastRefreshedOnResult)) {
            return false;
        }
        final RetrieveStatisticsLastRefreshedOnResult that = (RetrieveStatisticsLastRefreshedOnResult) o;
        return new EqualsBuilder()
                .append(lastRefreshedOn(), that.lastRefreshedOn())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(lastRefreshedOn())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lastRefreshedOn", lastRefreshedOn())
                .toString();
    }
}