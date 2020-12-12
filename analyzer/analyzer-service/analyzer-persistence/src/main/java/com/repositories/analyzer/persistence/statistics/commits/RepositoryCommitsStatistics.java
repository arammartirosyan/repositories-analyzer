package com.repositories.analyzer.persistence.statistics.commits;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

public class RepositoryCommitsStatistics {

    @Field(name = "commits")
    private List<RepositoryCommit> commits;

    @Field(name = "last_refreshed_on")
    private LocalDateTime lastRefreshedOn;

    /* Will be used by reflection */
    protected RepositoryCommitsStatistics() {
        super();
    }

    public RepositoryCommitsStatistics(final List<RepositoryCommit> commits) {
        this.commits = commits;
        this.lastRefreshedOn = LocalDateTime.now();
    }

    public List<RepositoryCommit> getCommits() {
        return commits;
    }

    public LocalDateTime getLastRefreshedOn() {
        return lastRefreshedOn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("commits", getCommits())
                .append("lastRefreshedOn", getLastRefreshedOn())
                .toString();
    }
}