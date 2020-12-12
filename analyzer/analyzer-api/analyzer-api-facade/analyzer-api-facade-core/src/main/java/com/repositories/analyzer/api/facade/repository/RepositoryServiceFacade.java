package com.repositories.analyzer.api.facade.repository;

import com.repositories.analyzer.api.model.repository.RepositoriesSearchRequest;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchResponse;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsRequest;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsResponse;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsRequest;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsResponse;

public interface RepositoryServiceFacade {

    RepositoriesSearchResponse searchRepositories(RepositoriesSearchRequest request);

    RetrieveRepositoryCommitsResponse retrieveCommits(RetrieveRepositoryCommitsRequest request);

    RetrieveRepositoryContributorsResponse retrieveContributors(RetrieveRepositoryContributorsRequest request);
}