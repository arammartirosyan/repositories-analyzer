package com.repositories.analyzer.api.facade.repository.utils;

import com.repositories.analyzer.api.model.repository.commits.RepositoryCommitDto;
import com.repositories.analyzer.api.model.repository.commits.RepositoryCommitsAggregationDto;
import com.repositories.analyzer.common.usecase.Failure;
import com.repositories.analyzer.common.usecase.FailureDto;
import com.repositories.analyzer.github.client.model.response.GitHubRepository;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommit;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommitInfo;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommitter;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryContributor;
import com.repositories.analyzer.github.client.model.response.GitHubUser;
import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import com.repositories.analyzer.service.repository.details.RepositoryDetails;
import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ServiceFacadeTestUtils {

    private ServiceFacadeTestUtils() {
        throw new UnsupportedOperationException();
    }

    public static GitHubRepository gitHubRepository() {
        return gitHubRepository(uuid());
    }

    public static GitHubRepository gitHubRepository(final String id) {
        final GitHubRepository gitHubRepository = new GitHubRepository();
        gitHubRepository.setId(id);
        gitHubRepository.setName(uuid());
        gitHubRepository.setFullName(uuid());
        gitHubRepository.setDescription(uuid());
        gitHubRepository.setLanguage(uuid());
        gitHubRepository.setCreatedOn(LocalDateTime.now());
        return gitHubRepository;
    }

    public static RepositoryDocumentCreationParameter creationParameter() {
        return RepositoryDocumentCreationParameter.of(uuid(), uuid(), uuid(), uuid(), uuid(), LocalDateTime.now());
    }

    public static RepositoryDocumentModificationParameter modificationParameter() {
        return RepositoryDocumentModificationParameter.of(uuid(), uuid(), uuid(), uuid(), uuid());
    }

    public static RepositoryDetails repositoryDetails() {
        return RepositoryDetails.of(uuid(), uuid(), uuid());
    }

    public static RepositoryCommitDetails repositoryCommitDetails() {
        return RepositoryCommitDetails.of(uuid(), uuid(), uuid(), LocalDate.now());
    }

    public static RepositoryCommitsAggregationDto repositoryCommitsAggregation() {
        final RepositoryCommitDto firstCommit = new RepositoryCommitDto();
        firstCommit.setMessage(uuid());
        firstCommit.setCommittedOn(LocalDate.now().minusDays(2));

        final List<RepositoryCommitDto> details = List.of(firstCommit);

        final RepositoryCommitsAggregationDto aggregation = new RepositoryCommitsAggregationDto();
        aggregation.setName(uuid());
        aggregation.setEmail(uuid());
        aggregation.setCount(details.size());
        aggregation.setDetails(details);

        return aggregation;
    }

    public static FailureDto failure(final Failure failure) {
        return new FailureDto(failure.code(), failure.reason());
    }

    public static GitHubRepositoryCommit gitHubRepositoryCommit() {
        final GitHubRepositoryCommit gitHubRepositoryCommit = new GitHubRepositoryCommit();
        gitHubRepositoryCommit.setAuthor(gitHubUser());
        gitHubRepositoryCommit.setCommitter(gitHubUser());
        gitHubRepositoryCommit.setCommitInfo(gitHubRepositoryCommitInfo());
        return gitHubRepositoryCommit;
    }

    public static GitHubRepositoryContributor gitHubRepositoryContributor() {
        final GitHubRepositoryContributor gitHubRepositoryContributor = new GitHubRepositoryContributor();
        gitHubRepositoryContributor.setAuthor(gitHubUser());
        return gitHubRepositoryContributor;
    }

    public static RepositoryContributorDetails repositoryContributorDetails() {
        return RepositoryContributorDetails.of(uuid(), uuid(), "User");
    }

    private static GitHubRepositoryCommitInfo gitHubRepositoryCommitInfo() {
        final GitHubRepositoryCommitInfo gitHubRepositoryCommitInfo = new GitHubRepositoryCommitInfo();
        gitHubRepositoryCommitInfo.setMessage(uuid());
        gitHubRepositoryCommitInfo.setAuthor(gitHubRepositoryCommitter());
        gitHubRepositoryCommitInfo.setCommitter(gitHubRepositoryCommitter());
        return gitHubRepositoryCommitInfo;
    }

    private static GitHubRepositoryCommitter gitHubRepositoryCommitter() {
        final GitHubRepositoryCommitter gitHubRepositoryCommitter = new GitHubRepositoryCommitter();
        gitHubRepositoryCommitter.setName(uuid());
        gitHubRepositoryCommitter.setEmail(uuid());
        gitHubRepositoryCommitter.setDate(LocalDate.now());
        return gitHubRepositoryCommitter;
    }

    private static GitHubUser gitHubUser() {
        final GitHubUser gitHubUser = new GitHubUser();
        gitHubUser.setId(uuid());
        gitHubUser.setType("User");
        gitHubUser.setLogin(uuid());
        return gitHubUser;
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }
}
