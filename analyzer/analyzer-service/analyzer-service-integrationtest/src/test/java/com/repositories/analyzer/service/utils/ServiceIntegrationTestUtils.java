package com.repositories.analyzer.service.utils;

import com.repositories.analyzer.persistence.RepositoryDocument;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommit;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributor;
import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorParameter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public final class ServiceIntegrationTestUtils {

    private ServiceIntegrationTestUtils() {
        throw new UnsupportedOperationException();
    }

    public static RepositoryDocument repositoryDocument() {
        return new RepositoryDocument(uuid(), uuid(), uuid(), uuid(), uuid(), LocalDateTime.now());
    }

    public static RepositoryDocumentCreationParameter creationParameter() {
        return RepositoryDocumentCreationParameter.of(uuid(), uuid(), uuid(), uuid(), uuid(), LocalDateTime.now());
    }

    public static RepositoryDocumentModificationParameter modificationParameter(final String remoteId) {
        return RepositoryDocumentModificationParameter.of(remoteId, uuid(), uuid(), uuid(), uuid());
    }

    public static RefreshRepositoryCommitParameter refreshCommitParameter() {
        return RefreshRepositoryCommitParameter.of(uuid(), uuid(), uuid(), LocalDate.now());
    }

    public static RefreshRepositoryContributorParameter refreshContributorParameter() {
        return RefreshRepositoryContributorParameter.of(uuid(), uuid(), uuid());
    }

    public static RepositoryDocument repositoryDocumentWithCommits() {
        final RepositoryDocument repositoryDocument = repositoryDocument();
        repositoryDocument.refreshCommitsStatistics(List.of(repositoryCommit(), repositoryCommit()));
        return repositoryDocument;
    }

    public static RepositoryDocument repositoryDocumentWithContributors() {
        final RepositoryDocument repositoryDocument = repositoryDocument();
        repositoryDocument.refreshContributorsStatistics(List.of(repositoryContributor(), repositoryContributor()));
        return repositoryDocument;
    }

    public static boolean isEqual(final LocalDateTime first, final LocalDateTime second) {
        return first.truncatedTo(ChronoUnit.MILLIS).isEqual(second.truncatedTo(ChronoUnit.MILLIS));
    }

    private static RepositoryCommit repositoryCommit() {
        return new RepositoryCommit(uuid(), uuid(), uuid(), LocalDate.now());
    }

    private static RepositoryContributor repositoryContributor() {
        return new RepositoryContributor(uuid(), uuid(), "User");
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }
}