package com.repositories.analyzer.persistence.repository;

import com.repositories.analyzer.persistence.RepositoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RepositoryDocumentRepository extends MongoRepository<RepositoryDocument, String> {

    @Query(
            value = "{'_id' : ?0 }",
            fields = "{ '_id' : 1, 'statistics.commits_statistics.commits' : 1}"
    )
    Optional<RepositoryDocument> findRepositoryCommitsStatistics(String repositoryUid);

    @Query(
            value = "{'_id' : ?0 }",
            fields = "{ '_id' : 1, 'statistics.contributors_statistics.contributors' : 1}"
    )
    Optional<RepositoryDocument> findRepositoryContributorsStatistics(String repositoryUid);

    @Query(
            value = "{'_id' : ?0 }",
            fields = "{ '_id' : 1, 'statistics.commits_statistics.last_refreshed_on' : 1}"
    )
    Optional<RepositoryDocument> findRepositoryCommitsStatisticsLastRefreshedOn(String repositoryUid);

    @Query(
            value = "{'_id' : ?0 }",
            fields = "{ '_id' : 1, 'statistics.contributors_statistics.last_refreshed_on' : 1}"
    )
    Optional<RepositoryDocument> findRepositoryContributorsStatisticsLastRefreshedOn(String repositoryUid);

    @Query(
            value = "{'_id' : ?0 }",
            fields = "{ '_id' : 1, 'remote_id' : 1, 'name' : 1, 'full_name' : 1, 'description' : 1, 'language' : 1}"
    )
    Optional<RepositoryDocument> findRepositoryDetails(String repositoryUid);

    @Query(
            value = "{ remote_id : { $in : ?0 } }",
            fields = "{ '_id' : 1, 'remote_id' : 1}"
    )
    Collection<RepositoryDocument> findAllRepositoryRefsByRemoteIdIn(Collection<String> remoteIds);

    Collection<RepositoryDocument> findAllByRemoteIdIn(Collection<String> remoteIds);
}