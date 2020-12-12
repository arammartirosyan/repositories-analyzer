package com.repositories.analyzer.api.facade.repository.sync;

import com.repositories.analyzer.github.client.model.response.GitHubRepository;
import com.repositories.analyzer.service.repository.RepositoryService;
import com.repositories.analyzer.service.repository.common.RepositoryRef;
import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class DefaultRepositoriesSyncParameterBuilder implements RepositoriesSyncParameterBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRepositoriesSyncParameterBuilder.class);

    private final RepositoryService repositoryService;

    DefaultRepositoriesSyncParameterBuilder(final RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public RepositoriesSyncParameter syncParameterFor(final Collection<GitHubRepository> repositories) {
        Assert.notEmpty(repositories, "Null or empty collection was passed as an argument for parameter 'repositories'.");
        logger.debug("Building repositories syc parameter for given repositories: {}.", repositories);
        final Set<String> existsInSystemIds = repositoryService.repositoryRefs(
                repositories.stream()
                        .map(GitHubRepository::getId)
                        .collect(Collectors.toUnmodifiableList())
        ).stream()
                .map(RepositoryRef::remoteId)
                .collect(Collectors.toSet());

        final Map<Boolean, List<GitHubRepository>> existingRepos = repositories.stream()
                .collect(Collectors.partitioningBy(repo -> existsInSystemIds.contains(repo.getId())));

        return RepositoriesSyncParameter.of(
                existingRepos.get(Boolean.FALSE).stream()
                        .map(gitHubRepository ->
                                RepositoryDocumentCreationParameter.of(
                                        gitHubRepository.getId(),
                                        gitHubRepository.getName(),
                                        gitHubRepository.getFullName(),
                                        gitHubRepository.getDescription(),
                                        gitHubRepository.getLanguage(),
                                        gitHubRepository.getCreatedOn()
                                )
                        ).collect(Collectors.toUnmodifiableList()),
                existingRepos.get(Boolean.TRUE).stream()
                        .map(gitHubRepository ->
                                RepositoryDocumentModificationParameter.of(
                                        gitHubRepository.getId(),
                                        gitHubRepository.getName(),
                                        gitHubRepository.getFullName(),
                                        gitHubRepository.getDescription(),
                                        gitHubRepository.getLanguage()
                                )
                        ).collect(Collectors.toUnmodifiableList())
        );
    }
}