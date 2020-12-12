package com.repositories.analyzer.persistence.statistics.contributors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

public class RepositoryContributorsStatistics {

    @Field(name = "contributors")
    private List<RepositoryContributor> contributors;

    @Field(name = "last_refreshed_on")
    private LocalDateTime lastRefreshedOn;

    /* Will be used by reflection */
    protected RepositoryContributorsStatistics() {
        super();
    }

    public RepositoryContributorsStatistics(final List<RepositoryContributor> contributors) {
        this.contributors = contributors;
        this.lastRefreshedOn = LocalDateTime.now();
    }

    public List<RepositoryContributor> getContributors() {
        return contributors;
    }

    public LocalDateTime getLastRefreshedOn() {
        return lastRefreshedOn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("contributors", getContributors())
                .append("lastRefreshedOn", getLastRefreshedOn())
                .toString();
    }
}