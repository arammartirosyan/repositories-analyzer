package com.repositories.analyzer.api.client;

import com.repositories.analyzer.api.model.repository.RepositoriesSearchResponse;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsResponse;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

@FeignClient(name = "analyzerResourceClient", url = "${analyzer.service.url:http://localhost:8080}")
public interface AnalyzerResourceClient {

    @GetMapping(value = "/repositories/search")
    RepositoriesSearchResponse searchRepositories(@RequestParam("query") @NotBlank String query);

    @GetMapping(value = "/repositories/{repositoryUid}/commits")
    RetrieveRepositoryCommitsResponse retrieveRepositoryCommits(@PathVariable("repositoryUid") String repositoryUid);

    @GetMapping(value = "/repositories/{repositoryUid}/contributors")
    RetrieveRepositoryContributorsResponse retrieveRepositoryContributors(@PathVariable("repositoryUid") String repositoryUid);
}