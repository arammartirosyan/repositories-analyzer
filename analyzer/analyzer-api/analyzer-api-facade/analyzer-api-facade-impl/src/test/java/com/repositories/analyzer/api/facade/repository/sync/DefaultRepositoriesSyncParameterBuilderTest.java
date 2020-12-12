package com.repositories.analyzer.api.facade.repository.sync;

import com.repositories.analyzer.api.facade.AbstractServiceFacadeTest;
import com.repositories.analyzer.api.facade.repository.utils.ServiceFacadeTestUtils;
import com.repositories.analyzer.github.client.model.response.GitHubRepository;
import com.repositories.analyzer.service.repository.RepositoryService;
import com.repositories.analyzer.service.repository.common.RepositoryRef;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;

public class DefaultRepositoriesSyncParameterBuilderTest extends AbstractServiceFacadeTest {

    @Mock
    private RepositoryService repositoryService;

    @InjectMocks
    private DefaultRepositoriesSyncParameterBuilder repositoriesSyncParameterBuilder;

    @Override
    public void verifyNoMoreMocksInteractions() {
        verifyNoMoreInteractions(repositoryService);
    }

    @Test
    public void testSyncParameterFor() {
        final String firstId = uuid();
        final String secondId = uuid();
        final String thirdId = uuid();

        final List<GitHubRepository> gitHubRepositories = List.of(
                ServiceFacadeTestUtils.gitHubRepository(firstId),
                ServiceFacadeTestUtils.gitHubRepository(secondId),
                ServiceFacadeTestUtils.gitHubRepository(thirdId)
        );
        final List<RepositoryRef> existingRepositories = List.of(RepositoryRef.of(uuid(), firstId));

        when(repositoryService.repositoryRefs(List.of(firstId, secondId, thirdId))).thenReturn(existingRepositories);

        final RepositoriesSyncParameter syncParameter = repositoriesSyncParameterBuilder.syncParameterFor(gitHubRepositories);

        Assert.assertEquals(existingRepositories.size(), syncParameter.reposToModify().size());
        Assert.assertEquals(gitHubRepositories.size() - existingRepositories.size(), syncParameter.reposToCreate().size());

        verify(repositoryService).repositoryRefs(List.of(firstId, secondId, thirdId));
    }
}