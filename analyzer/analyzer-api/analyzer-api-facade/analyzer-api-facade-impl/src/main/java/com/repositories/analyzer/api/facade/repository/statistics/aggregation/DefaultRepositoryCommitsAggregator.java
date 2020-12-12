package com.repositories.analyzer.api.facade.repository.statistics.aggregation;

import com.repositories.analyzer.api.model.repository.commits.RepositoryCommitDto;
import com.repositories.analyzer.api.model.repository.commits.RepositoryCommitsAggregationDto;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
class DefaultRepositoryCommitsAggregator implements RepositoryCommitsAggregator {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRepositoryCommitsAggregator.class);

    @Override
    public Collection<RepositoryCommitsAggregationDto> aggregateByUser(final Collection<RepositoryCommitDetails> commits) {
        Assert.notNull(commits, "Null collection was passed as an argument for parameter 'commits'.");
        logger.debug("Aggregating repository commits: {} by user.", commits);
        return commits.stream().collect(Collectors.groupingBy(RepositoryCommitDetails::email))
                .values().stream()
                .map(userCommits -> {
                    final RepositoryCommitDetails firstCommit = userCommits.get(0);
                    final RepositoryCommitsAggregationDto aggregation = new RepositoryCommitsAggregationDto();
                    aggregation.setName(firstCommit.name());
                    aggregation.setEmail(firstCommit.email());
                    aggregation.setCount(userCommits.size());
                    aggregation.setDetails(
                            userCommits.stream()
                                    .map(details -> new RepositoryCommitDto(details.message(), details.committedOn()))
                                    .collect(Collectors.toList())
                    );
                    return aggregation;
                }).collect(Collectors.toList());
    }
}