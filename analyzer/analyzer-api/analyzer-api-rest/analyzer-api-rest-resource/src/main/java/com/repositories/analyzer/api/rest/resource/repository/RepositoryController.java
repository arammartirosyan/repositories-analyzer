package com.repositories.analyzer.api.rest.resource.repository;

import com.repositories.analyzer.api.facade.repository.RepositoryServiceFacade;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchRequest;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchResponse;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsRequest;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsResponse;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsRequest;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(
        value = "/repositories",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
class RepositoryController {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryController.class);

    private final RepositoryServiceFacade repositoryServiceFacade;

    RepositoryController(final RepositoryServiceFacade repositoryServiceFacade) {
        this.repositoryServiceFacade = repositoryServiceFacade;
    }

    @GetMapping(value = "/search")
    public ResponseEntity<RepositoriesSearchResponse> searchRepositories(@RequestParam("query") @NotBlank final String query) {
        logger.info("Searching repositories for given query: {}.", query);
        return ResponseEntity.ok(repositoryServiceFacade.searchRepositories(new RepositoriesSearchRequest(query)));
    }

    @GetMapping(value = "/{repositoryUid}/commits")
    public ResponseEntity<RetrieveRepositoryCommitsResponse> getCommits(@PathVariable("repositoryUid") final String repositoryUid) {
        logger.info("Getting repository commits for given uid: {}.", repositoryUid);
        return ResponseEntity.ok(repositoryServiceFacade.retrieveCommits(new RetrieveRepositoryCommitsRequest(repositoryUid)));
    }

    @GetMapping(value = "/{repositoryUid}/contributors")
    public ResponseEntity<RetrieveRepositoryContributorsResponse> getContributors(@PathVariable("repositoryUid") final String repositoryUid) {
        logger.info("Getting repository contributors for given uid: {}.", repositoryUid);
        return ResponseEntity.ok(repositoryServiceFacade.retrieveContributors(new RetrieveRepositoryContributorsRequest(repositoryUid)));
    }
}