package com.repositories.analyzer.api.facade.repository;

import com.repositories.analyzer.api.facade.AbstractServiceFacadeTest;
import com.repositories.analyzer.api.facade.repository.statistics.StatisticsUpToDateChecker;
import com.repositories.analyzer.api.facade.repository.statistics.aggregation.RepositoryCommitsAggregator;
import com.repositories.analyzer.api.facade.repository.sync.RepositoriesSyncParameter;
import com.repositories.analyzer.api.facade.repository.sync.RepositoriesSyncParameterBuilder;
import com.repositories.analyzer.api.facade.repository.utils.ServiceFacadeTestUtils;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchRequest;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchResponse;
import com.repositories.analyzer.api.model.repository.RepositoryDto;
import com.repositories.analyzer.api.model.repository.commits.RepositoryCommitsAggregationDto;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsRequest;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsResponse;
import com.repositories.analyzer.api.model.repository.contributors.RepositoryContributorDto;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsRequest;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsResponse;
import com.repositories.analyzer.common.usecase.CommonFailures;
import com.repositories.analyzer.github.client.GitHubRepositoriesClient;
import com.repositories.analyzer.github.client.GitHubSearchClient;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoriesSearchRequest;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryCommitsRequest;
import com.repositories.analyzer.github.client.model.request.GitHubRepositoryContributorsRequest;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchItems;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoriesSearchResponse;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryCommitsResponse;
import com.repositories.analyzer.github.client.model.response.GitHubRepositoryContributorsResponse;
import com.repositories.analyzer.service.repository.RepositoryService;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationParameter;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationResult;
import com.repositories.analyzer.service.repository.details.RetrieveRepositoryDetailsFailure;
import com.repositories.analyzer.service.repository.details.RetrieveRepositoryDetailsResult;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationParameter;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationResult;
import com.repositories.analyzer.service.repository.statistics.RepositoryStatisticsService;
import com.repositories.analyzer.service.repository.statistics.commits.RetrieveRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;
import com.repositories.analyzer.service.repository.statistics.contributors.RetrieveRepositoryContributorsResult;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class DefaultRepositoryServiceFacadeTest extends AbstractServiceFacadeTest {

    private static final int COMMITS_ITEMS_PER_PAGE = 100;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private GitHubSearchClient gitHubSearchClient;

    @Mock
    private GitHubRepositoriesClient gitHubRepositoriesClient;

    @Mock
    private StatisticsUpToDateChecker statisticsUpToDateChecker;

    @Mock
    private RepositoriesSyncParameterBuilder syncParameterBuilder;

    @Mock
    private RepositoryStatisticsService repositoryStatisticsService;

    @Mock
    private RepositoryCommitsAggregator repositoryCommitsAggregator;

    private RepositoryServiceFacade repositoryServiceFacade;

    @Before
    public void setUp() {
        repositoryServiceFacade = new DefaultRepositoryServiceFacade(
                repositoryService,
                gitHubSearchClient,
                gitHubRepositoriesClient,
                statisticsUpToDateChecker,
                syncParameterBuilder,
                repositoryStatisticsService,
                repositoryCommitsAggregator,
                COMMITS_ITEMS_PER_PAGE
        );
    }

    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(
                repositoryService,
                gitHubSearchClient,
                gitHubRepositoriesClient,
                statisticsUpToDateChecker,
                syncParameterBuilder,
                repositoryStatisticsService,
                repositoryCommitsAggregator
        );
    }

    @Test
    public void testSearchRepositoriesWhenApiThreshholdReached() {
        final String query = uuid();

        final GitHubRepositoriesSearchResponse searchResponse = new GitHubRepositoriesSearchResponse();
        searchResponse.setThreshHoldReached(true);

        when(gitHubSearchClient.search(GitHubRepositoriesSearchRequest.of(query))).thenReturn(searchResponse);

        final RepositoriesSearchResponse response = repositoryServiceFacade.searchRepositories(new RepositoriesSearchRequest(query));
        Assert.assertTrue(response.isFailed());
        Assert.assertEquals(
                List.of(ServiceFacadeTestUtils.failure(CommonFailures.API_THRESHHOLD_REACHED_FAILURE)),
                response.getFailures()
        );
        verify(gitHubSearchClient).search(GitHubRepositoriesSearchRequest.of(query));
    }

    @Test
    public void testSearchRepositoriesEmptyResponse() {
        final String query = uuid();

        final GitHubRepositoriesSearchResponse searchResponse = new GitHubRepositoriesSearchResponse();
        searchResponse.setResponse(new GitHubRepositoriesSearchItems());

        when(gitHubSearchClient.search(GitHubRepositoriesSearchRequest.of(query))).thenReturn(searchResponse);

        final RepositoriesSearchResponse response = repositoryServiceFacade.searchRepositories(new RepositoriesSearchRequest(query));
        Assert.assertFalse(response.isFailed());
        Assert.assertTrue(response.getRepositories().isEmpty());

        verify(gitHubSearchClient).search(GitHubRepositoriesSearchRequest.of(query));
    }

    @Test
    public void testSearchRepositories() {
        final String query = uuid();

        final GitHubRepositoriesSearchItems searchItems = new GitHubRepositoriesSearchItems();
        searchItems.setItems(List.of(ServiceFacadeTestUtils.gitHubRepository(), ServiceFacadeTestUtils.gitHubRepository()));

        final GitHubRepositoriesSearchResponse searchResponse = new GitHubRepositoriesSearchResponse();
        searchResponse.setResponse(searchItems);

        final RepositoriesSyncParameter syncParameter = RepositoriesSyncParameter.of(
                List.of(ServiceFacadeTestUtils.creationParameter()), List.of(ServiceFacadeTestUtils.modificationParameter())
        );
        final RepositoryDocumentBulkCreationResult creationResult = RepositoryDocumentBulkCreationResult.of(
                List.of(ServiceFacadeTestUtils.repositoryDetails())
        );
        final RepositoryDocumentBulkModificationResult modificationResult = RepositoryDocumentBulkModificationResult.of(
                List.of(ServiceFacadeTestUtils.repositoryDetails())
        );
        when(gitHubSearchClient.search(GitHubRepositoriesSearchRequest.of(query))).thenReturn(searchResponse);
        when(syncParameterBuilder.syncParameterFor(searchResponse.getResponse().getItems())).thenReturn(syncParameter);
        when(repositoryService.bulkCreate(RepositoryDocumentBulkCreationParameter.of(syncParameter.reposToCreate()))).thenReturn(creationResult);
        when(repositoryService.bulkModify(RepositoryDocumentBulkModificationParameter.of(syncParameter.reposToModify()))).thenReturn(modificationResult);

        final RepositoriesSearchResponse response = repositoryServiceFacade.searchRepositories(new RepositoriesSearchRequest(query));
        Assert.assertFalse(response.isFailed());
        Assert.assertEquals(creationResult.repositories().size() + modificationResult.repositories().size(), response.getRepositories().size());
        Stream.concat(creationResult.repositories().stream(), modificationResult.repositories().stream()).forEach(details ->
                Assert.assertTrue(response.getRepositories().contains(new RepositoryDto(details.uid(), details.name(), details.fullName())))
        );
        verify(gitHubSearchClient).search(GitHubRepositoriesSearchRequest.of(query));
        verify(syncParameterBuilder).syncParameterFor(searchResponse.getResponse().getItems());
        verify(repositoryService).bulkCreate(RepositoryDocumentBulkCreationParameter.of(syncParameter.reposToCreate()));
        verify(repositoryService).bulkModify(RepositoryDocumentBulkModificationParameter.of(syncParameter.reposToModify()));
    }

    @Test
    public void testRetrieveNotExistsRepositoryCommits() {
        final String repositoryUid = uuid();

        when(statisticsUpToDateChecker.commitsStatisticsUpToDate(repositoryUid)).thenReturn(false);
        when(repositoryService.retrieveDetails(repositoryUid)).thenReturn(RetrieveRepositoryDetailsResult.repositoryNotFound());

        final RetrieveRepositoryCommitsResponse response = repositoryServiceFacade.retrieveCommits(
                new RetrieveRepositoryCommitsRequest(repositoryUid)
        );
        Assert.assertTrue(response.isFailed());
        Assert.assertEquals(
                List.of(ServiceFacadeTestUtils.failure(RetrieveRepositoryDetailsFailure.REPOSITORY_NOT_FOUND)),
                response.getFailures()
        );
        verify(statisticsUpToDateChecker).commitsStatisticsUpToDate(repositoryUid);
        verify(repositoryService).retrieveDetails(repositoryUid);
    }

    @Test
    public void testRetrieveCommitsFromLocal() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryCommitsResult commitsResult = RetrieveRepositoryCommitsResult.of(
                List.of(ServiceFacadeTestUtils.repositoryCommitDetails(), ServiceFacadeTestUtils.repositoryCommitDetails())
        );
        final List<RepositoryCommitsAggregationDto> aggregations = List.of(ServiceFacadeTestUtils.repositoryCommitsAggregation());

        when(statisticsUpToDateChecker.commitsStatisticsUpToDate(repositoryUid)).thenReturn(true);
        when(repositoryStatisticsService.retrieveCommits(repositoryUid)).thenReturn(commitsResult);
        when(repositoryCommitsAggregator.aggregateByUser(commitsResult.commits())).thenReturn(aggregations);

        final RetrieveRepositoryCommitsResponse response = repositoryServiceFacade.retrieveCommits(new RetrieveRepositoryCommitsRequest(repositoryUid));
        Assert.assertFalse(response.isFailed());
        Assert.assertEquals(aggregations, response.getCommits());
    }

    @Test
    public void testRetrieveCommitsFromRemoteWhenApiThreshholdReached() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryDetailsResult detailsResult = RetrieveRepositoryDetailsResult.of(
                ServiceFacadeTestUtils.repositoryDetails()
        );
        final GitHubRepositoryCommitsRequest commitsRequest = GitHubRepositoryCommitsRequest.of(
                detailsResult.details().fullName(), COMMITS_ITEMS_PER_PAGE
        );
        final GitHubRepositoryCommitsResponse commitsResponse = new GitHubRepositoryCommitsResponse();
        commitsResponse.setThreshHoldReached(true);

        when(statisticsUpToDateChecker.commitsStatisticsUpToDate(repositoryUid)).thenReturn(false);
        when(repositoryService.retrieveDetails(repositoryUid)).thenReturn(detailsResult);
        when(gitHubRepositoriesClient.retrieveCommits(commitsRequest)).thenReturn(commitsResponse);

        final RetrieveRepositoryCommitsResponse response = repositoryServiceFacade.retrieveCommits(new RetrieveRepositoryCommitsRequest(repositoryUid));
        Assert.assertTrue(response.isFailed());
        Assert.assertEquals(
                List.of(ServiceFacadeTestUtils.failure(CommonFailures.API_THRESHHOLD_REACHED_FAILURE)),
                response.getFailures()
        );
        verify(statisticsUpToDateChecker).commitsStatisticsUpToDate(repositoryUid);
        verify(repositoryService).retrieveDetails(repositoryUid);
        verify(gitHubRepositoriesClient).retrieveCommits(commitsRequest);
    }

    @Test
    public void testRetrieveEmptyCommitsFromRemote() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryDetailsResult detailsResult = RetrieveRepositoryDetailsResult.of(
                ServiceFacadeTestUtils.repositoryDetails()
        );
        final GitHubRepositoryCommitsRequest commitsRequest = GitHubRepositoryCommitsRequest.of(
                detailsResult.details().fullName(), COMMITS_ITEMS_PER_PAGE
        );
        final GitHubRepositoryCommitsResponse commitsResponse = new GitHubRepositoryCommitsResponse();
        commitsResponse.setResponse(List.of());

        when(statisticsUpToDateChecker.commitsStatisticsUpToDate(repositoryUid)).thenReturn(false);
        when(repositoryService.retrieveDetails(repositoryUid)).thenReturn(detailsResult);
        when(gitHubRepositoriesClient.retrieveCommits(commitsRequest)).thenReturn(commitsResponse);
        when(repositoryCommitsAggregator.aggregateByUser(List.of())).thenReturn(List.of());

        final RetrieveRepositoryCommitsResponse response = repositoryServiceFacade.retrieveCommits(new RetrieveRepositoryCommitsRequest(repositoryUid));
        Assert.assertFalse(response.isFailed());
        Assert.assertTrue(response.getCommits().isEmpty());

        verify(statisticsUpToDateChecker).commitsStatisticsUpToDate(repositoryUid);
        verify(repositoryService).retrieveDetails(repositoryUid);
        verify(gitHubRepositoriesClient).retrieveCommits(commitsRequest);
        verify(repositoryCommitsAggregator).aggregateByUser(List.of());
    }

    @Test
    public void testRetrieveCommitsFromRemote() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryDetailsResult detailsResult = RetrieveRepositoryDetailsResult.of(
                ServiceFacadeTestUtils.repositoryDetails()
        );
        final GitHubRepositoryCommitsRequest commitsRequest = GitHubRepositoryCommitsRequest.of(
                detailsResult.details().fullName(), COMMITS_ITEMS_PER_PAGE
        );
        final GitHubRepositoryCommitsResponse commitsResponse = new GitHubRepositoryCommitsResponse();
        commitsResponse.setResponse(List.of(ServiceFacadeTestUtils.gitHubRepositoryCommit()));

        final RefreshRepositoryCommitsResult refreshResult = RefreshRepositoryCommitsResult.of(
                List.of(ServiceFacadeTestUtils.repositoryCommitDetails())
        );
        final List<RepositoryCommitsAggregationDto> aggregations = List.of(ServiceFacadeTestUtils.repositoryCommitsAggregation());

        when(statisticsUpToDateChecker.commitsStatisticsUpToDate(repositoryUid)).thenReturn(false);
        when(repositoryService.retrieveDetails(repositoryUid)).thenReturn(detailsResult);
        when(gitHubRepositoriesClient.retrieveCommits(commitsRequest)).thenReturn(commitsResponse);
        when(repositoryService.refreshRepositoryCommits(any(RefreshRepositoryCommitsParameter.class))).thenReturn(refreshResult);
        when(repositoryCommitsAggregator.aggregateByUser(refreshResult.commits())).thenReturn(aggregations);

        final RetrieveRepositoryCommitsResponse response = repositoryServiceFacade.retrieveCommits(new RetrieveRepositoryCommitsRequest(repositoryUid));
        Assert.assertFalse(response.isFailed());
        Assert.assertEquals(aggregations, response.getCommits());

        verify(statisticsUpToDateChecker).commitsStatisticsUpToDate(repositoryUid);
        verify(repositoryService).retrieveDetails(repositoryUid);
        verify(gitHubRepositoriesClient).retrieveCommits(commitsRequest);
        verify(repositoryService).refreshRepositoryCommits(any(RefreshRepositoryCommitsParameter.class));
        verify(repositoryCommitsAggregator).aggregateByUser(refreshResult.commits());
    }

    @Test
    public void testRetrieveNotExistsRepositoryContributors() {
        final String repositoryUid = uuid();

        when(statisticsUpToDateChecker.contributorsStatisticsUpToDate(repositoryUid)).thenReturn(false);
        when(repositoryService.retrieveDetails(repositoryUid)).thenReturn(RetrieveRepositoryDetailsResult.repositoryNotFound());

        final RetrieveRepositoryContributorsResponse response = repositoryServiceFacade.retrieveContributors(
                new RetrieveRepositoryContributorsRequest(repositoryUid)
        );
        Assert.assertTrue(response.isFailed());
        Assert.assertEquals(
                List.of(ServiceFacadeTestUtils.failure(RetrieveRepositoryDetailsFailure.REPOSITORY_NOT_FOUND)),
                response.getFailures()
        );
        verify(statisticsUpToDateChecker).contributorsStatisticsUpToDate(repositoryUid);
        verify(repositoryService).retrieveDetails(repositoryUid);
    }

    @Test
    public void testRetrieveContributorsFromLocal() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryContributorsResult contributorsResult = RetrieveRepositoryContributorsResult.of(
                List.of(
                        ServiceFacadeTestUtils.repositoryContributorDetails(),
                        ServiceFacadeTestUtils.repositoryContributorDetails()
                )
        );
        when(statisticsUpToDateChecker.contributorsStatisticsUpToDate(repositoryUid)).thenReturn(true);
        when(repositoryStatisticsService.retrieveContributors(repositoryUid)).thenReturn(contributorsResult);

        final RetrieveRepositoryContributorsResponse response = repositoryServiceFacade.retrieveContributors(
                new RetrieveRepositoryContributorsRequest(repositoryUid)
        );
        Assert.assertFalse(response.isFailed());
        assertEquals(contributorsResult.contributors(), response.getContributors());

        verify(statisticsUpToDateChecker).contributorsStatisticsUpToDate(repositoryUid);
        verify(repositoryStatisticsService).retrieveContributors(repositoryUid);
    }

    @Test
    public void testRetrieveContributorsFromRemoteWhenApiThreshholdReached() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryDetailsResult detailsResult = RetrieveRepositoryDetailsResult.of(
                ServiceFacadeTestUtils.repositoryDetails()
        );
        final GitHubRepositoryContributorsRequest contributorsRequest = GitHubRepositoryContributorsRequest.of(
                detailsResult.details().fullName()
        );
        final GitHubRepositoryContributorsResponse contributorsResponse = new GitHubRepositoryContributorsResponse();
        contributorsResponse.setThreshHoldReached(true);

        when(statisticsUpToDateChecker.contributorsStatisticsUpToDate(repositoryUid)).thenReturn(false);
        when(repositoryService.retrieveDetails(repositoryUid)).thenReturn(detailsResult);
        when(gitHubRepositoriesClient.retrieveContributors(contributorsRequest)).thenReturn(contributorsResponse);

        final RetrieveRepositoryContributorsResponse response = repositoryServiceFacade.retrieveContributors(
                new RetrieveRepositoryContributorsRequest(repositoryUid)
        );
        Assert.assertTrue(response.isFailed());
        Assert.assertEquals(
                List.of(ServiceFacadeTestUtils.failure(CommonFailures.API_THRESHHOLD_REACHED_FAILURE)),
                response.getFailures()
        );
        verify(statisticsUpToDateChecker).contributorsStatisticsUpToDate(repositoryUid);
        verify(repositoryService).retrieveDetails(repositoryUid);
        verify(gitHubRepositoriesClient).retrieveContributors(contributorsRequest);
    }

    @Test
    public void testRetrieveEmptyContributorsFromRemote() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryDetailsResult detailsResult = RetrieveRepositoryDetailsResult.of(
                ServiceFacadeTestUtils.repositoryDetails()
        );
        final GitHubRepositoryContributorsRequest contributorsRequest = GitHubRepositoryContributorsRequest.of(
                detailsResult.details().fullName()
        );
        final GitHubRepositoryContributorsResponse contributorsResponse = new GitHubRepositoryContributorsResponse();
        contributorsResponse.setResponse(List.of());

        when(statisticsUpToDateChecker.contributorsStatisticsUpToDate(repositoryUid)).thenReturn(false);
        when(repositoryService.retrieveDetails(repositoryUid)).thenReturn(detailsResult);
        when(gitHubRepositoriesClient.retrieveContributors(contributorsRequest)).thenReturn(contributorsResponse);

        final RetrieveRepositoryContributorsResponse response = repositoryServiceFacade.retrieveContributors(
                new RetrieveRepositoryContributorsRequest(repositoryUid)
        );
        Assert.assertFalse(response.isFailed());
        Assert.assertTrue(response.getContributors().isEmpty());

        verify(statisticsUpToDateChecker).contributorsStatisticsUpToDate(repositoryUid);
        verify(repositoryService).retrieveDetails(repositoryUid);
        verify(gitHubRepositoriesClient).retrieveContributors(contributorsRequest);
    }

    @Test
    public void testRetrieveContributorsFromRemote() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryDetailsResult detailsResult = RetrieveRepositoryDetailsResult.of(
                ServiceFacadeTestUtils.repositoryDetails()
        );
        final GitHubRepositoryContributorsRequest contributorsRequest = GitHubRepositoryContributorsRequest.of(
                detailsResult.details().fullName()
        );
        final GitHubRepositoryContributorsResponse contributorsResponse = new GitHubRepositoryContributorsResponse();
        contributorsResponse.setResponse(List.of(ServiceFacadeTestUtils.gitHubRepositoryContributor()));

        final RefreshRepositoryContributorsResult refreshResult = RefreshRepositoryContributorsResult.of(
                List.of(ServiceFacadeTestUtils.repositoryContributorDetails())
        );
        when(statisticsUpToDateChecker.contributorsStatisticsUpToDate(repositoryUid)).thenReturn(false);
        when(repositoryService.retrieveDetails(repositoryUid)).thenReturn(detailsResult);
        when(gitHubRepositoriesClient.retrieveContributors(contributorsRequest)).thenReturn(contributorsResponse);
        when(repositoryService.refreshRepositoryContributors(any(RefreshRepositoryContributorsParameter.class))).thenReturn(refreshResult);

        final RetrieveRepositoryContributorsResponse response = repositoryServiceFacade.retrieveContributors(
                new RetrieveRepositoryContributorsRequest(repositoryUid)
        );
        Assert.assertFalse(response.isFailed());
        assertEquals(refreshResult.contributors(), response.getContributors());

        verify(statisticsUpToDateChecker).contributorsStatisticsUpToDate(repositoryUid);
        verify(repositoryService).retrieveDetails(repositoryUid);
        verify(gitHubRepositoriesClient).retrieveContributors(contributorsRequest);
        verify(repositoryService).refreshRepositoryContributors(any(RefreshRepositoryContributorsParameter.class));
    }

    private static void assertEquals(final Collection<RepositoryContributorDetails> details,
                                     final Collection<RepositoryContributorDto> contributors) {
        Assert.assertEquals(details.size(), contributors.size());
        details.forEach(detail -> {
            final RepositoryContributorDto repositoryContributor = new RepositoryContributorDto();
            repositoryContributor.setRemoteId(detail.remoteId());
            repositoryContributor.setLogin(detail.login());
            repositoryContributor.setType(detail.type());
            Assert.assertTrue(contributors.contains(repositoryContributor));
        });
    }
}