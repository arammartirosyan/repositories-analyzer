package com.repositories.analyzer.api.facade.repository.sync;

import com.repositories.analyzer.github.client.model.response.GitHubRepository;

import java.util.Collection;

public interface RepositoriesSyncParameterBuilder {

    RepositoriesSyncParameter syncParameterFor(Collection<GitHubRepository> repositories);
}