package com.repositories.analyzer.service.utils;

import com.repositories.analyzer.persistence.RepositoryDocument;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommit;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributor;
import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorParameter;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ServiceTestUtils {

    private ServiceTestUtils() {
        throw new UnsupportedOperationException();
    }

    public static RepositoryDocument repositoryDocument() {
        final RepositoryDocument repositoryDocument = new RepositoryDocument(
                uuid(), uuid(), uuid(), uuid(), uuid(), LocalDateTime.now()
        );
        setUid(repositoryDocument, uuid());
        return repositoryDocument;
    }

    public static RepositoryDocument repositoryDocument(final String uid, final String remoteId) {
        final RepositoryDocument repositoryDocument = new RepositoryDocument(
                remoteId, uuid(), uuid(), uuid(), uuid(), LocalDateTime.now()
        );
        setUid(repositoryDocument, uid);
        return repositoryDocument;
    }

    public static RepositoryDocument repositoryDocumentWithCommits(final String uid) {
        final RepositoryDocument repositoryDocument = repositoryDocument(uid, uuid());
        repositoryDocument.refreshCommitsStatistics(List.of(repositoryCommit(), repositoryCommit()));
        return repositoryDocument;
    }

    public static RepositoryDocument repositoryDocumentWithContributors(final String uid) {
        final RepositoryDocument repositoryDocument = repositoryDocument(uid, uuid());
        repositoryDocument.refreshContributorsStatistics(List.of(repositoryContributor(), repositoryContributor()));
        return repositoryDocument;
    }

    public static RepositoryDocumentCreationParameter repositoryDocumentCreationParameter() {
        return RepositoryDocumentCreationParameter.of(uuid(), uuid(), uuid(), uuid(), uuid(), LocalDateTime.now());
    }

    public static RepositoryDocumentModificationParameter repositoryDocumentModificationParameter(final String remoteId) {
        return RepositoryDocumentModificationParameter.of(remoteId, uuid(), uuid(), uuid(), uuid());
    }

    public static RefreshRepositoryCommitParameter refreshRepositoryCommitParameter() {
        return RefreshRepositoryCommitParameter.of(uuid(), uuid(), uuid(), LocalDate.now());
    }

    public static RefreshRepositoryContributorParameter refreshRepositoryContributorParameter() {
        return RefreshRepositoryContributorParameter.of(uuid(), uuid(), "User");
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

    private static void setUid(final RepositoryDocument document, final String uid) {
        ReflectionTestUtils.setField(document, "uid", uid);
    }
}