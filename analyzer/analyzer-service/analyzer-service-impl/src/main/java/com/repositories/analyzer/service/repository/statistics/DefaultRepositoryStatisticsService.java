package com.repositories.analyzer.service.repository.statistics;

import com.repositories.analyzer.persistence.RepositoryDocument;
import com.repositories.analyzer.persistence.repository.RepositoryDocumentRepository;
import com.repositories.analyzer.persistence.statistics.RepositoryStatistics;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommit;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommitsStatistics;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributor;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributorsStatistics;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import com.repositories.analyzer.service.repository.statistics.commits.RetrieveRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.common.RetrieveStatisticsLastRefreshedOnResult;
import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;
import com.repositories.analyzer.service.repository.statistics.contributors.RetrieveRepositoryContributorsResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
class DefaultRepositoryStatisticsService implements RepositoryStatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRepositoryStatisticsService.class);

    private final RepositoryDocumentRepository repositoryDocumentRepository;

    DefaultRepositoryStatisticsService(final RepositoryDocumentRepository repositoryDocumentRepository) {
        this.repositoryDocumentRepository = repositoryDocumentRepository;
    }

    @Override
    public RetrieveRepositoryCommitsResult retrieveCommits(final String repositoryUid) {
        Assert.hasText(repositoryUid, "Null or empty text was passed as an argument for parameter 'repositoryUid'.");
        logger.debug("Retrieving repository commits for given uid: {}.", repositoryUid);
        return repositoryDocumentRepository.findRepositoryCommitsStatistics(repositoryUid)
                .filter(RepositoryDocument::commitsStatisticsAvailable)
                .map(RepositoryDocument::getStatistics)
                .map(RepositoryStatistics::getCommitsStatistics)
                .map(RepositoryCommitsStatistics::getCommits)
                .map(DefaultRepositoryStatisticsService::listOfCommits)
                .map(RetrieveRepositoryCommitsResult::of)
                .orElseGet(RetrieveRepositoryCommitsResult::empty);
    }

    @Override
    public RetrieveRepositoryContributorsResult retrieveContributors(final String repositoryUid) {
        Assert.hasText(repositoryUid, "Null or empty text was passed as an argument for parameter 'repositoryUid'.");
        logger.debug("Retrieving repository contributors for given uid: {}.", repositoryUid);
        return repositoryDocumentRepository.findRepositoryContributorsStatistics(repositoryUid)
                .filter(RepositoryDocument::contributorsStatisticsAvailable)
                .map(RepositoryDocument::getStatistics)
                .map(RepositoryStatistics::getContributorsStatistics)
                .map(RepositoryContributorsStatistics::getContributors)
                .map(DefaultRepositoryStatisticsService::listOfContributors)
                .map(RetrieveRepositoryContributorsResult::of)
                .orElseGet(RetrieveRepositoryContributorsResult::empty);
    }

    @Override
    public RetrieveStatisticsLastRefreshedOnResult retrieveCommitsLastRefreshedOn(final String repositoryUid) {
        Assert.hasText(repositoryUid, "Null or empty text was passed as an argument for parameter 'repositoryUid'.");
        logger.debug("Retrieving repository commits last refreshed on for given uid: {}.", repositoryUid);
        return repositoryDocumentRepository.findRepositoryCommitsStatisticsLastRefreshedOn(repositoryUid)
                .map(RepositoryDocument::getStatistics)
                .map(RepositoryStatistics::getCommitsStatistics)
                .map(RepositoryCommitsStatistics::getLastRefreshedOn)
                .map(RetrieveStatisticsLastRefreshedOnResult::of)
                .orElseGet(RetrieveStatisticsLastRefreshedOnResult::notRefreshed);
    }

    @Override
    public RetrieveStatisticsLastRefreshedOnResult retrieveContributorsLastRefreshedOn(final String repositoryUid) {
        Assert.hasText(repositoryUid, "Null or empty text was passed as an argument for parameter 'repositoryUid'.");
        logger.debug("Retrieving repository contributors last refreshed on for given uid: {}.", repositoryUid);
        return repositoryDocumentRepository.findRepositoryContributorsStatisticsLastRefreshedOn(repositoryUid)
                .map(RepositoryDocument::getStatistics)
                .map(RepositoryStatistics::getContributorsStatistics)
                .map(RepositoryContributorsStatistics::getLastRefreshedOn)
                .map(RetrieveStatisticsLastRefreshedOnResult::of)
                .orElseGet(RetrieveStatisticsLastRefreshedOnResult::notRefreshed);
    }

    private static Collection<RepositoryCommitDetails> listOfCommits(final Collection<RepositoryCommit> commits) {
        return commits.stream()
                .map(commit ->
                        RepositoryCommitDetails.of(
                                commit.getName(), commit.getEmail(), commit.getMessage(), commit.getCommittedOn()
                        )
                ).collect(Collectors.toList());
    }

    private static Collection<RepositoryContributorDetails> listOfContributors(final Collection<RepositoryContributor> contributors) {
        return contributors.stream()
                .map(contributor ->
                        RepositoryContributorDetails.of(
                                contributor.getRemoteId(), contributor.getLogin(), contributor.getType()
                        )
                ).collect(Collectors.toList());
    }
}