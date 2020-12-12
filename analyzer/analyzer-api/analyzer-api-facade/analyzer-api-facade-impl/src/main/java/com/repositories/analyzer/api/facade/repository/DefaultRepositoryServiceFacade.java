package com.repositories.analyzer.api.facade.repository;

import com.repositories.analyzer.api.facade.repository.statistics.StatisticsUpToDateChecker;
import com.repositories.analyzer.api.facade.repository.statistics.aggregation.RepositoryCommitsAggregator;
import com.repositories.analyzer.api.facade.repository.sync.RepositoriesSyncParameter;
import com.repositories.analyzer.api.facade.repository.sync.RepositoriesSyncParameterBuilder;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchRequest;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchResponse;
import com.repositories.analyzer.api.model.repository.RepositoryDto;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsRequest;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsResponse;
import com.repositories.analyzer.api.model.repository.contributors.RepositoryContributorDto;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsRequest;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsResponse;
import com.repositories.analyzer.common.api.model.AbstractResponse;
import com.repositories.analyzer.common.usecase.CommonFailures;
import com.repositories.analyzer.common.usecase.Failure;
import com.repositories.analyzer.github.client.GitHubRepositoriesClient;
import com.repositories.analyzer.github.client.GitHubSearchClient;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoriesSearchRequest;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryCommitsRequest;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryContributorsRequest;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchResponse;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommit;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommitsResponse;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryContributor;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryContributorsResponse;
import com.repositories.analyzer.service.repository.RepositoryService;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationParameter;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationResult;
import com.repositories.analyzer.service.repository.details.RetrieveRepositoryDetailsResult;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationParameter;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationResult;
import com.repositories.analyzer.service.repository.statistics.RepositoryStatisticsService;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import com.repositories.analyzer.service.repository.statistics.commits.RetrieveRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;
import com.repositories.analyzer.service.repository.statistics.contributors.RetrieveRepositoryContributorsResult;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class DefaultRepositoryServiceFacade implements RepositoryServiceFacade {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRepositoryServiceFacade.class);

    private final RepositoryService repositoryService;

    private final GitHubSearchClient gitHubSearchClient;

    private final GitHubRepositoriesClient gitHubRepositoriesClient;

    private final StatisticsUpToDateChecker statisticsUpToDateChecker;

    private final RepositoriesSyncParameterBuilder syncParameterBuilder;

    private final RepositoryStatisticsService repositoryStatisticsService;

    private final RepositoryCommitsAggregator repositoryCommitsAggregator;

    private final int commitsItemsPerPage;

    DefaultRepositoryServiceFacade(
            final RepositoryService repositoryService,
            final GitHubSearchClient gitHubSearchClient,
            final GitHubRepositoriesClient gitHubRepositoriesClient,
            final StatisticsUpToDateChecker statisticsUpToDateChecker,
            final RepositoriesSyncParameterBuilder syncParameterBuilder,
            final RepositoryStatisticsService repositoryStatisticsService,
            final RepositoryCommitsAggregator repositoryCommitsAggregator,
            @Value("${repository.commits.items.per.page:100}") final int commitsItemsPerPage
    ) {
        this.repositoryService = repositoryService;
        this.gitHubSearchClient = gitHubSearchClient;
        this.gitHubRepositoriesClient = gitHubRepositoriesClient;
        this.statisticsUpToDateChecker = statisticsUpToDateChecker;
        this.syncParameterBuilder = syncParameterBuilder;
        this.repositoryStatisticsService = repositoryStatisticsService;
        this.repositoryCommitsAggregator = repositoryCommitsAggregator;
        this.commitsItemsPerPage = commitsItemsPerPage;
    }

    @Override
    public RepositoriesSearchResponse searchRepositories(final RepositoriesSearchRequest request) {
        Assert.notNull(request, "Null was passed as an argument for parameter 'request'.");
        logger.info("Searching repositories for given request: {}.", request);
        final GitHubRepositoriesSearchResponse searchResponse = gitHubSearchClient.search(
                GitHubRepositoriesSearchRequest.of(request.getQuery())
        );
        if (searchResponse.isThreshHoldReached()) {
            logger.warn("Search repositories failed. API threshhold limit reached.");
            return apiThreshHoldReachedResponse(RepositoriesSearchResponse::new);
        }
        if (CollectionUtils.isEmpty(searchResponse.getResponse().getItems())) {
            logger.debug("There is no any repositories for given request: {}.", request);
            return repositoriesSearchEmptyResponse();
        }
        final RepositoriesSyncParameter syncParameter = syncParameterBuilder.syncParameterFor(
                searchResponse.getResponse().getItems()
        );
        final RepositoryDocumentBulkCreationResult creationResult = repositoryService.bulkCreate(
                RepositoryDocumentBulkCreationParameter.of(syncParameter.reposToCreate())
        );
        if (creationResult.hasFailures()) {
            logger.warn("Bulk create repositories failed with: {}.", creationResult.failures());
            return new RepositoriesSearchResponse(creationResult.failures());
        }
        final RepositoryDocumentBulkModificationResult modificationResult = repositoryService.bulkModify(
                RepositoryDocumentBulkModificationParameter.of(syncParameter.reposToModify())
        );
        if (modificationResult.hasFailures()) {
            logger.warn("Bulk modify repositories failed with: {}.", modificationResult.failures());
            return new RepositoriesSearchResponse(modificationResult.failures());
        }
        final RepositoriesSearchResponse repositoriesSearchResponse = new RepositoriesSearchResponse();
        repositoriesSearchResponse.setRepositories(
                Stream.concat(creationResult.repositories().stream(), modificationResult.repositories().stream())
                        .map(repository -> new RepositoryDto(repository.uid(), repository.name(), repository.fullName()))
                        .collect(Collectors.toList())
        );
        logger.debug("Done searching repositories. Request: {} , Response: {}.", request, repositoriesSearchResponse);
        return repositoriesSearchResponse;
    }

    @Override
    public RetrieveRepositoryCommitsResponse retrieveCommits(final RetrieveRepositoryCommitsRequest request) {
        Assert.notNull(request, "Null was passed as an argument for parameter 'request'.");
        return statisticsUpToDateChecker.commitsStatisticsUpToDate(request.getRepositoryUid())
                ? retrieveCommitsFromLocal(request.getRepositoryUid())
                : retrieveCommitsFromRemote(request.getRepositoryUid());
    }

    @Override
    public RetrieveRepositoryContributorsResponse retrieveContributors(final RetrieveRepositoryContributorsRequest request) {
        Assert.notNull(request, "Null was passed as an argument for parameter 'request'.");
        return statisticsUpToDateChecker.contributorsStatisticsUpToDate(request.getRepositoryUid())
                ? retrieveContributorsFromLocal(request.getRepositoryUid())
                : retrieveContributorsFromRemote(request.getRepositoryUid());
    }

    private RetrieveRepositoryCommitsResponse retrieveCommitsFromLocal(final String repositoryUid) {
        logger.info("Retrieving repository commits from local for given repo uid: {}.", repositoryUid);
        final RetrieveRepositoryCommitsResult result = repositoryStatisticsService.retrieveCommits(repositoryUid);
        if (result.hasFailures()) {
            logger.warn("Retrieving repository commits with uid: {} failed with: {}.", repositoryUid, result.failures());
            return new RetrieveRepositoryCommitsResponse(result.failures());
        }
        return retrieveRepositoryCommitsResponse(result.commits());
    }

    private RetrieveRepositoryContributorsResponse retrieveContributorsFromLocal(final String repositoryUid) {
        logger.info("Retrieving repository contributors from local for given repo uid: {}.", repositoryUid);
        final RetrieveRepositoryContributorsResult result = repositoryStatisticsService.retrieveContributors(repositoryUid);
        if (result.hasFailures()) {
            logger.warn("Retrieving repository contributors with uid: {} failed with: {}.", repositoryUid, result.failures());
            return new RetrieveRepositoryContributorsResponse(result.failures());
        }
        return retrieveRepositoryContributorsResponse(result.contributors());
    }

    private RetrieveRepositoryCommitsResponse retrieveCommitsFromRemote(final String repositoryUid) {
        logger.info("Retrieving repository commits from remote for given repo uid: {}.", repositoryUid);
        final RetrieveRepositoryDetailsResult detailsResult = repositoryService.retrieveDetails(repositoryUid);
        if (detailsResult.hasFailures()) {
            logger.warn("Retrieving repository details with uid: {} failed with: {}.", repositoryUid, detailsResult.failures());
            return new RetrieveRepositoryCommitsResponse(detailsResult.failures());
        }
        final GitHubRepositoryCommitsResponse response = gitHubRepositoriesClient.retrieveCommits(
                GitHubRepositoryCommitsRequest.of(detailsResult.details().fullName(), commitsItemsPerPage)
        );
        if (response.isThreshHoldReached()) {
            logger.warn("Retrieve repository commits failed. API threshhold limit reached.");
            return apiThreshHoldReachedResponse(RetrieveRepositoryCommitsResponse::new);
        }
        if (CollectionUtils.isEmpty(response.getResponse())) {
            logger.debug("There is no any commit for repository with uid: {}.", repositoryUid);
            return retrieveRepositoryCommitsResponse(List.of());
        }
        final RefreshRepositoryCommitsResult refreshResult = refreshRepositoryCommits(repositoryUid, response.getResponse());
        if (refreshResult.hasFailures()) {
            logger.warn("Refresh repository commits with uid: {} failed with: {}.", repositoryUid, refreshResult.failures());
            return new RetrieveRepositoryCommitsResponse(detailsResult.failures());
        }
        return retrieveRepositoryCommitsResponse(refreshResult.commits());
    }

    private RetrieveRepositoryContributorsResponse retrieveContributorsFromRemote(final String repositoryUid) {
        logger.info("Retrieving repository contributors from remote for given repo uid: {}.", repositoryUid);
        final RetrieveRepositoryDetailsResult detailsResult = repositoryService.retrieveDetails(repositoryUid);
        if (detailsResult.hasFailures()) {
            logger.warn("Retrieving repository details with uid: {} failed with: {}.", repositoryUid, detailsResult.failures());
            return new RetrieveRepositoryContributorsResponse(detailsResult.failures());
        }
        final GitHubRepositoryContributorsResponse response = gitHubRepositoriesClient.retrieveContributors(
                GitHubRepositoryContributorsRequest.of(detailsResult.details().fullName())
        );
        if (response.isThreshHoldReached()) {
            logger.warn("Retrieve repository contributors failed. API threshhold limit reached.");
            return apiThreshHoldReachedResponse(RetrieveRepositoryContributorsResponse::new);
        }
        if (CollectionUtils.isEmpty(response.getResponse())) {
            logger.debug("There is no any contributors for repository with uid: {}.", repositoryUid);
            return retrieveRepositoryContributorsResponse(List.of());
        }
        final RefreshRepositoryContributorsResult refreshResult = refreshRepositoryContributors(repositoryUid, response.getResponse());
        if (refreshResult.hasFailures()) {
            logger.warn("Refresh repository contributors with uid: {} failed with: {}.", repositoryUid, refreshResult.failures());
            return new RetrieveRepositoryContributorsResponse(refreshResult.failures());
        }
        return retrieveRepositoryContributorsResponse(refreshResult.contributors());
    }

    private RefreshRepositoryCommitsResult refreshRepositoryCommits(final String repositoryUid,
                                                                    final List<GitHubRepositoryCommit> commits) {
        return repositoryService.refreshRepositoryCommits(
                RefreshRepositoryCommitsParameter.of(
                        repositoryUid,
                        commits.stream()
                                .map(GitHubRepositoryCommit::getCommitInfo)
                                .map(commitInfo ->
                                        RefreshRepositoryCommitParameter.of(
                                                commitInfo.getCommitter().getName(),
                                                commitInfo.getCommitter().getEmail(),
                                                commitInfo.getMessage(),
                                                commitInfo.getCommitter().getDate()
                                        )
                                ).collect(Collectors.toList()
                        )
                )
        );
    }

    private RefreshRepositoryContributorsResult refreshRepositoryContributors(final String repositoryUid,
                                                                              final List<GitHubRepositoryContributor> contributors) {
        return repositoryService.refreshRepositoryContributors(
                RefreshRepositoryContributorsParameter.of(
                        repositoryUid,
                        contributors.stream()
                                .map(GitHubRepositoryContributor::getAuthor)
                                .map(contributor ->
                                        RefreshRepositoryContributorParameter.of(
                                                contributor.getId(), contributor.getLogin(), contributor.getType()
                                        )
                                ).collect(Collectors.toList())
                )
        );
    }

    private RetrieveRepositoryCommitsResponse retrieveRepositoryCommitsResponse(final Collection<RepositoryCommitDetails> commits) {
        final RetrieveRepositoryCommitsResponse response = new RetrieveRepositoryCommitsResponse();
        response.setCommits(repositoryCommitsAggregator.aggregateByUser(commits));
        return response;
    }

    private static RetrieveRepositoryContributorsResponse retrieveRepositoryContributorsResponse(final Collection<RepositoryContributorDetails> contributors) {
        final RetrieveRepositoryContributorsResponse contributorsResponse = new RetrieveRepositoryContributorsResponse();
        contributorsResponse.setContributors(
                contributors.stream()
                        .map(details -> new RepositoryContributorDto(details.remoteId(), details.login(), details.type()))
                        .collect(Collectors.toList())
        );
        return contributorsResponse;
    }

    private static <R extends AbstractResponse> R apiThreshHoldReachedResponse(final Function<List<Failure>, R> function) {
        return function.apply(List.of(CommonFailures.API_THRESHHOLD_REACHED_FAILURE));
    }

    private static RepositoriesSearchResponse repositoriesSearchEmptyResponse() {
        final RepositoriesSearchResponse searchResponse = new RepositoriesSearchResponse();
        searchResponse.setRepositories(List.of());
        return searchResponse;
    }
}