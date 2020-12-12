package com.repositories.analyzer.api.rest.resource.repository;

import com.repositories.analyzer.api.facade.repository.RepositoryServiceFacade;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchRequest;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchResponse;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsRequest;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsResponse;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsRequest;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsResponse;
import com.repositories.analyzer.api.rest.resource.AbstractRestResourceTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;

public class RepositoryControllerTest extends AbstractRestResourceTest {

    @Mock
    private RepositoryServiceFacade repositoryServiceFacade;

    @InjectMocks
    private RepositoryController repositoryController;

    @After
    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(repositoryServiceFacade);
    }

    @Test
    public void testSearchRepositories() {
        final String query = uuid();

        final RepositoriesSearchResponse response = new RepositoriesSearchResponse();
        response.setRepositories(List.of());

        final RepositoriesSearchRequest request = new RepositoriesSearchRequest(query);

        when(repositoryServiceFacade.searchRepositories(request)).thenReturn(response);

        final ResponseEntity<RepositoriesSearchResponse> responseEntity = repositoryController.searchRepositories(query);

        Assert.assertEquals(response, responseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(repositoryServiceFacade).searchRepositories(request);
    }

    @Test
    public void testGetCommits() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryCommitsResponse response = new RetrieveRepositoryCommitsResponse();
        response.setCommits(List.of());

        final RetrieveRepositoryCommitsRequest request = new RetrieveRepositoryCommitsRequest(repositoryUid);

        when(repositoryServiceFacade.retrieveCommits(request)).thenReturn(response);

        final ResponseEntity<RetrieveRepositoryCommitsResponse> responseEntity = repositoryController.getCommits(repositoryUid);

        Assert.assertEquals(response, responseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(repositoryServiceFacade).retrieveCommits(request);
    }

    @Test
    public void testGetContributors() {
        final String repositoryUid = uuid();

        final RetrieveRepositoryContributorsResponse response = new RetrieveRepositoryContributorsResponse();
        response.setContributors(List.of());

        final RetrieveRepositoryContributorsRequest request = new RetrieveRepositoryContributorsRequest(repositoryUid);

        when(repositoryServiceFacade.retrieveContributors(request)).thenReturn(response);

        final ResponseEntity<RetrieveRepositoryContributorsResponse> responseEntity = repositoryController.getContributors(repositoryUid);

        Assert.assertEquals(response, responseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(repositoryServiceFacade).retrieveContributors(request);
    }
}