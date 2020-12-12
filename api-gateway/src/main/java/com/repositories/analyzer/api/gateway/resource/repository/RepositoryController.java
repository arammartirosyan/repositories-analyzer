package com.repositories.analyzer.api.gateway.resource.repository;

import com.repositories.analyzer.api.client.AnalyzerResourceClient;
import com.repositories.analyzer.api.model.repository.RepositoriesSearchResponse;
import com.repositories.analyzer.api.model.repository.commits.RetrieveRepositoryCommitsResponse;
import com.repositories.analyzer.api.model.repository.contributors.RetrieveRepositoryContributorsResponse;
import com.repositories.analyzer.common.api.model.AbstractResponse;
import com.repositories.analyzer.common.usecase.CommonFailures;
import com.repositories.analyzer.common.usecase.FailureDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@RestController
@RequestMapping(
        value = "/repositories",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
class RepositoryController {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryController.class);

    private static final long DEFAULT_API_TIMEOUT = 5_000L;

    private final Executor executor;

    private final AnalyzerResourceClient analyzerResourceClient;

    RepositoryController(
            @Qualifier("msCallsExecutor") final Executor executor,
            final AnalyzerResourceClient analyzerResourceClient
    ) {
        this.executor = executor;
        this.analyzerResourceClient = analyzerResourceClient;
    }

    @GetMapping(value = "/search")
    @Operation(
            description = "Searching repositories for given query",
            responses = @ApiResponse(
                    content = @Content(schema = @Schema(oneOf = RepositoriesSearchResponse.class))
            )
    )
    public DeferredResult<ResponseEntity<RepositoriesSearchResponse>> searchRepositories(@RequestParam(value = "query") @NotBlank final String query) {
        logger.info("Searching repositories for given query: {}.", query);
        return deferredResponse(
                CompletableFuture.supplyAsync(() -> analyzerResourceClient.searchRepositories(query), executor),
                RepositoriesSearchResponse::new
        );
    }

    @GetMapping(value = "/{repositoryUid}/commits")
    @Operation(
            description = "Getting repository commits for given uid",
            responses = @ApiResponse(
                    content = @Content(schema = @Schema(oneOf = RetrieveRepositoryCommitsResponse.class))
            )
    )
    public DeferredResult<ResponseEntity<RetrieveRepositoryCommitsResponse>> getCommits(@PathVariable("repositoryUid") final String repositoryUid) {
        logger.info("Getting repository commits for given uid: {}.", repositoryUid);
        return deferredResponse(
                CompletableFuture.supplyAsync(() -> analyzerResourceClient.retrieveRepositoryCommits(repositoryUid), executor),
                RetrieveRepositoryCommitsResponse::new
        );
    }

    @GetMapping(value = "/{repositoryUid}/contributors")
    @Operation(
            description = "Getting repository contributors for given uid",
            responses = @ApiResponse(
                    content = @Content(schema = @Schema(oneOf = RetrieveRepositoryContributorsResponse.class))
            )
    )
    public DeferredResult<ResponseEntity<RetrieveRepositoryContributorsResponse>> getContributors(@PathVariable("repositoryUid") final String repositoryUid) {
        logger.info("Getting repository contributors for given uid: {}.", repositoryUid);
        return deferredResponse(
                CompletableFuture.supplyAsync(() -> analyzerResourceClient.retrieveRepositoryContributors(repositoryUid), executor),
                RetrieveRepositoryContributorsResponse::new
        );
    }

    private <R extends AbstractResponse> DeferredResult<ResponseEntity<R>> deferredResponse(final CompletableFuture<R> responsePromise,
                                                                                            final Supplier<R> constructor) {
        final DeferredResult<ResponseEntity<R>> deferredResponse = new DeferredResult<>(DEFAULT_API_TIMEOUT);
        responsePromise.whenComplete((response, throwable) -> {
            if (throwable == null) {
                deferredResponse.setResult(responseFor(HttpStatus.OK, response));
            } else {
                logger.error("Request failed.", throwable);
                deferredResponse.setErrorResult(internalServerError(constructor));
            }
        });
        return deferredResponse;
    }

    private static <R extends AbstractResponse> ResponseEntity<R> internalServerError(final Supplier<R> constructor) {
        final R response = constructor.get();
        response.setFailures(
                List.of(
                        new FailureDto(
                                CommonFailures.INTERNAL_SERVER_ERROR.code(),
                                CommonFailures.INTERNAL_SERVER_ERROR.reason()
                        )
                )
        );
        return responseFor(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    private static <R extends AbstractResponse> ResponseEntity<R> responseFor(final HttpStatus httpStatus, final R body) {
        return ResponseEntity.status(httpStatus).body(body);
    }
}