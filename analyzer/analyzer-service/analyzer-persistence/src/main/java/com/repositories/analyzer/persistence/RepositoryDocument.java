package com.repositories.analyzer.persistence;

import com.repositories.analyzer.persistence.statistics.RepositoryStatistics;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommit;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommitsStatistics;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributor;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributorsStatistics;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "repositories")
public class RepositoryDocument {

    @Id
    private String uid;

    @Indexed(unique = true)
    @Field(name = "remote_id")
    private String remoteId;

    @Field(name = "name")
    private String name;

    @Field(name = "full_name")
    private String fullName;

    @Field(name = "description")
    private String description;

    @Field(name = "language")
    private String language;

    @Field(name = "statistics")
    private RepositoryStatistics statistics;

    @Field(name = "created_on")
    private LocalDateTime createdOn;

    @Field(name = "remote_created_on")
    private LocalDateTime remoteCreatedOn;

    /* Will be used by reflection */
    protected RepositoryDocument() {
        super();
    }

    public RepositoryDocument(
            final String remoteId,
            final String name,
            final String fullName,
            final String description,
            final String language,
            final LocalDateTime remoteCreatedOn
    ) {
        this.remoteId = remoteId;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.language = language;
        this.createdOn = LocalDateTime.now();
        this.remoteCreatedOn = remoteCreatedOn;
    }

    public String getUid() {
        return uid;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public RepositoryStatistics getStatistics() {
        return statistics;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getRemoteCreatedOn() {
        return remoteCreatedOn;
    }

    public boolean commitsStatisticsAvailable() {
        return getStatistics() != null && getStatistics().getCommitsStatistics() != null;
    }

    public boolean contributorsStatisticsAvailable() {
        return getStatistics() != null && getStatistics().getContributorsStatistics() != null;
    }

    public RepositoryDocument modifyName(final String name) {
        this.name = name;
        return this;
    }

    public RepositoryDocument modifyFullName(final String fullName) {
        this.fullName = fullName;
        return this;
    }

    public RepositoryDocument modifyDescription(final String description) {
        this.description = description;
        return this;
    }

    public RepositoryDocument modifyLanguage(final String language) {
        this.language = language;
        return this;
    }

    public RepositoryDocument refreshCommitsStatistics(final List<RepositoryCommit> commits) {
        if (statistics != null) {
            statistics.refreshCommitsStatistics(commits);
        } else {
            statistics = new RepositoryStatistics(new RepositoryCommitsStatistics(commits));
        }
        return this;
    }

    public RepositoryDocument refreshContributorsStatistics(final List<RepositoryContributor> contributors) {
        if (statistics != null) {
            statistics.refreshContributorsStatistics(contributors);
        } else {
            statistics = new RepositoryStatistics(new RepositoryContributorsStatistics(contributors));
        }
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uid", getUid())
                .append("remoteId", getRemoteId())
                .append("name", getName())
                .append("fullName", getFullName())
                .append("description", getDescription())
                .append("language", getLanguage())
                .append("statistics", getStatistics())
                .append("createdOn", getCreatedOn())
                .append("remoteCreatedOn", getRemoteCreatedOn())
                .toString();
    }
}