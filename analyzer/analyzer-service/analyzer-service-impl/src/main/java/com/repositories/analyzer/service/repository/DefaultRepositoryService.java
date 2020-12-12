package com.repositories.analyzer.service.repository;

import com.repositories.analyzer.persistence.RepositoryDocument;
import com.repositories.analyzer.persistence.repository.RepositoryDocumentRepository;
import com.repositories.analyzer.persistence.statistics.RepositoryStatistics;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommit;
import com.repositories.analyzer.persistence.statistics.commits.RepositoryCommitsStatistics;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributor;
import com.repositories.analyzer.persistence.statistics.contributors.RepositoryContributorsStatistics;
import com.repositories.analyzer.service.repository.common.RepositoryRef;
import com.repositories.analyzer.service.repository.creation.RepositoryDocumentCreationParameter;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationParameter;
import com.repositories.analyzer.service.repository.creation.bulk.RepositoryDocumentBulkCreationResult;
import com.repositories.analyzer.service.repository.details.RepositoryDetails;
import com.repositories.analyzer.service.repository.details.RetrieveRepositoryDetailsResult;
import com.repositories.analyzer.service.repository.modification.RepositoryDocumentModificationParameter;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationParameter;
import com.repositories.analyzer.service.repository.modification.bulk.RepositoryDocumentBulkModificationResult;
import com.repositories.analyzer.service.repository.statistics.commits.RepositoryCommitDetails;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsParameter;
import com.repositories.analyzer.service.repository.statistics.commits.refresh.RefreshRepositoryCommitsResult;
import com.repositories.analyzer.service.repository.statistics.contributors.RepositoryContributorDetails;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsParameter;
import com.repositories.analyzer.service.repository.statistics.contributors.refresh.RefreshRepositoryContributorsResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class DefaultRepositoryService implements RepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRepositoryService.class);

    private final RepositoryDocumentRepository repositoryDocumentRepository;

    DefaultRepositoryService(final RepositoryDocumentRepository repositoryDocumentRepository) {
        this.repositoryDocumentRepository = repositoryDocumentRepository;
    }

    @Override
    public Collection<RepositoryRef> repositoryRefs(final Collection<String> remoteIds) {
        Assert.notEmpty(remoteIds, "Null or empty collection was passed as an argument for parameter 'remoteIds'.");
        logger.debug("Retrieving repository refs for given remote ids: {}.", remoteIds);
        return repositoryDocumentRepository.findAllRepositoryRefsByRemoteIdIn(remoteIds).stream()
                .map(doc -> RepositoryRef.of(doc.getUid(), doc.getRemoteId()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public RetrieveRepositoryDetailsResult retrieveDetails(final String repositoryUid) {
        Assert.hasText(repositoryUid, "Null or empty text was passed as an argument for parameter 'repositoryUid'.");
        logger.debug("Retrieving repository details for given uid: {}.", repositoryUid);
        return repositoryDocumentRepository.findRepositoryDetails(repositoryUid)
                .map(DefaultRepositoryService::toRepositoryDetails)
                .map(RetrieveRepositoryDetailsResult::of)
                .orElseGet(RetrieveRepositoryDetailsResult::repositoryNotFound);
    }

    @Override
    public RepositoryDocumentBulkCreationResult bulkCreate(final RepositoryDocumentBulkCreationParameter parameter) {
        Assert.notNull(parameter, "Null was passed as an argument for parameter 'parameter'.");
        if (parameter.repositories().isEmpty()) {
            return RepositoryDocumentBulkCreationResult.of(List.of());
        }
        logger.debug("Bulk create repository documents for given parameter: {}.", parameter);
        return RepositoryDocumentBulkCreationResult.of(
                repositoryDocumentRepository.insert(
                        parameter.repositories().stream()
                                .map(DefaultRepositoryService::toRepositoryDocument)
                                .collect(Collectors.toList())
                ).stream()
                        .map(DefaultRepositoryService::toRepositoryDetails)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public RepositoryDocumentBulkModificationResult bulkModify(final RepositoryDocumentBulkModificationParameter parameter) {
        Assert.notNull(parameter, "Null was passed as an argument for parameter 'parameter'.");
        if (parameter.repositories().isEmpty()) {
            return RepositoryDocumentBulkModificationResult.of(List.of());
        }
        logger.debug("Bulk modify repository documents for given parameter: {}.", parameter);
        final Map<String, RepositoryDocumentModificationParameter> documentsToUpdateParameters = parameter.repositories()
                .stream()
                .collect(Collectors.toMap(RepositoryDocumentModificationParameter::remoteId, Function.identity()));

        return RepositoryDocumentBulkModificationResult.of(
                repositoryDocumentRepository.saveAll(
                        repositoryDocumentRepository.findAllByRemoteIdIn(documentsToUpdateParameters.keySet())
                                .stream()
                                .peek(document -> applyModifications(document, documentsToUpdateParameters.get(document.getRemoteId())))
                                .collect(Collectors.toList())
                ).stream()
                        .map(DefaultRepositoryService::toRepositoryDetails)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public RefreshRepositoryCommitsResult refreshRepositoryCommits(final RefreshRepositoryCommitsParameter parameter) {
        Assert.notNull(parameter, "Null was passed as an argument for parameter 'parameter'.");
        logger.debug("Refreshing repository commits for given parameter: {}.", parameter);
        return repositoryDocumentRepository.findById(parameter.repositoryUid())
                .map(document ->
                        document.refreshCommitsStatistics(
                                parameter.commits().stream()
                                        .map(DefaultRepositoryService::toRepositoryCommit)
                                        .collect(Collectors.toList())
                        )
                )
                .map(repositoryDocumentRepository::save)
                .map(RepositoryDocument::getStatistics)
                .map(RepositoryStatistics::getCommitsStatistics)
                .map(RepositoryCommitsStatistics::getCommits)
                .map(DefaultRepositoryService::toRepositoryCommitsDetails)
                .map(RefreshRepositoryCommitsResult::of)
                .orElseGet(RefreshRepositoryCommitsResult::repositoryNotFound);
    }

    @Override
    public RefreshRepositoryContributorsResult refreshRepositoryContributors(final RefreshRepositoryContributorsParameter parameter) {
        Assert.notNull(parameter, "Null was passed as an argument for parameter 'parameter'.");
        logger.debug("Refreshing repository contributors for given parameter: {}.", parameter);
        return repositoryDocumentRepository.findById(parameter.repositoryUid())
                .map(document ->
                        document.refreshContributorsStatistics(
                                parameter.contributors().stream()
                                        .map(DefaultRepositoryService::toRepositoryContributor)
                                        .collect(Collectors.toList())
                        )
                )
                .map(repositoryDocumentRepository::save)
                .map(RepositoryDocument::getStatistics)
                .map(RepositoryStatistics::getContributorsStatistics)
                .map(RepositoryContributorsStatistics::getContributors)
                .map(DefaultRepositoryService::toRepositoryContributorsDetails)
                .map(RefreshRepositoryContributorsResult::of)
                .orElseGet(RefreshRepositoryContributorsResult::repositoryNotFound);
    }

    private static void applyModifications(final RepositoryDocument document,
                                           final RepositoryDocumentModificationParameter parameter) {
        document.modifyName(parameter.name())
                .modifyFullName(parameter.fullName())
                .modifyDescription(parameter.description())
                .modifyLanguage(parameter.language());
    }

    private static RepositoryCommit toRepositoryCommit(final RefreshRepositoryCommitParameter parameter) {
        return new RepositoryCommit(parameter.name(), parameter.email(), parameter.message(), parameter.committedOn());
    }

    private static RepositoryContributor toRepositoryContributor(final RefreshRepositoryContributorParameter parameter) {
        return new RepositoryContributor(parameter.remoteId(), parameter.login(), parameter.type());
    }

    private static RepositoryDocument toRepositoryDocument(final RepositoryDocumentCreationParameter param) {
        return new RepositoryDocument(param.remoteId(), param.name(), param.fullName(), param.description(), param.language(), param.createdOn());
    }

    private static RepositoryDetails toRepositoryDetails(final RepositoryDocument document) {
        return RepositoryDetails.of(document.getUid(), document.getName(), document.getFullName());
    }

    private static RepositoryCommitDetails toRepositoryCommitDetails(final RepositoryCommit commit) {
        return RepositoryCommitDetails.of(commit.getName(), commit.getEmail(), commit.getMessage(), commit.getCommittedOn());
    }

    private static RepositoryContributorDetails toRepositoryContributorDetails(final RepositoryContributor contributor) {
        return RepositoryContributorDetails.of(contributor.getRemoteId(), contributor.getLogin(), contributor.getType());
    }

    private static Collection<RepositoryCommitDetails> toRepositoryCommitsDetails(final Collection<RepositoryCommit> commits) {
        return commits.stream()
                .map(DefaultRepositoryService::toRepositoryCommitDetails)
                .collect(Collectors.toList());
    }

    private static Collection<RepositoryContributorDetails> toRepositoryContributorsDetails(final Collection<RepositoryContributor> contributors) {
        return contributors.stream()
                .map(DefaultRepositoryService::toRepositoryContributorDetails)
                .collect(Collectors.toList());
    }
}