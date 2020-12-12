package com.repositories.analyzer.api.facade.repository.statistics.aggregation;

import com.repositories.analyzer.api.model.repository.commits.RepositoryCommitsAggregationDto;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;

import java.util.Collection;

public interface RepositoryCommitsAggregator {

    Collection<RepositoryCommitsAggregationDto> aggregateByUser(Collection<RepositoryCommitDetails> commits);
}