package com.repositories.analyzer.api.facade.repository.statistics.aggregation;

import com.repositories.analyzer.api.facade.AbstractServiceFacadeTest;
import com.repositories.analyzer.api.model.repository.commits.RepositoryCommitDto;
import com.repositories.analyzer.api.model.repository.commits.RepositoryCommitsAggregationDto;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class DefaultRepositoryCommitsAggregatorTest extends AbstractServiceFacadeTest {

    @InjectMocks
    private DefaultRepositoryCommitsAggregator repositoryCommitsAggregator;

    @After
    @Override
    public void verifyNoMoreMocksInteractions() {
    }

    @Test
    public void testAggregateBySingleUser() {
        final String name = uuid();
        final String email = uuid();
        final RepositoryCommitDetails firstCommit = repositoryCommitDetails(name, email);
        final RepositoryCommitDetails secondCommit = repositoryCommitDetails(name, email);
        final RepositoryCommitDetails thirdCommit = repositoryCommitDetails(name, email);
        final List<RepositoryCommitDetails> commitsDetails = List.of(firstCommit, secondCommit, thirdCommit);

        final Collection<RepositoryCommitsAggregationDto> aggregations = repositoryCommitsAggregator.aggregateByUser(commitsDetails);
        Assert.assertEquals(1, aggregations.size());

        final RepositoryCommitsAggregationDto aggregation = aggregations.iterator().next();
        Assert.assertEquals(commitsDetails.size(), aggregation.getCount().intValue());
        Assert.assertEquals(commitsDetails.size(), aggregation.getDetails().size());

        commitsDetails.forEach(commitDetails -> {
            Assert.assertEquals("name", commitDetails.name(), aggregation.getName());
            Assert.assertEquals("email", commitDetails.email(), aggregation.getEmail());
            aggregation.getDetails().forEach(details ->
                    Assert.assertTrue(
                            aggregation.getDetails()
                                    .contains(
                                            new RepositoryCommitDto(commitDetails.message(), commitDetails.committedOn()
                                            )
                                    )
                    ));
        });
    }

    private static RepositoryCommitDetails repositoryCommitDetails(final String name, final String email) {
        return RepositoryCommitDetails.of(name, email, uuid(), LocalDate.now());
    }
}