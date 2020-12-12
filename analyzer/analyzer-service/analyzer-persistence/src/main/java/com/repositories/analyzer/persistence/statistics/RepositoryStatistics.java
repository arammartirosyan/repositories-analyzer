package com.repositories.analyzer.persistence.statistics;

import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommit;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommitsStatistics;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributor;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributorsStatistics;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class RepositoryStatistics {

    @Field(name = "commits_statistics")
    private RepositoryCommitsStatistics commitsStatistics;

    @Field(name = "contributors_statistics")
    private RepositoryContributorsStatistics contributorsStatistics;

    /* Will be used by reflection */
    protected RepositoryStatistics() {
        super();
    }

    public RepositoryStatistics(final RepositoryCommitsStatistics commitsStatistics) {
        this.commitsStatistics = commitsStatistics;
    }

    public RepositoryStatistics(final RepositoryContributorsStatistics contributorsStatistics) {
        this.contributorsStatistics = contributorsStatistics;
    }

    public RepositoryCommitsStatistics getCommitsStatistics() {
        return commitsStatistics;
    }

    public RepositoryContributorsStatistics getContributorsStatistics() {
        return contributorsStatistics;
    }

    public void refreshCommitsStatistics(final List<RepositoryCommit> commits) {
        this.commitsStatistics = new RepositoryCommitsStatistics(commits);
    }

    public void refreshContributorsStatistics(final List<RepositoryContributor> contributors) {
        this.contributorsStatistics = new RepositoryContributorsStatistics(contributors);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("commitsStatistics", getCommitsStatistics())
                .append("contributorsStatistics", getContributorsStatistics())
                .toString();
    }
}