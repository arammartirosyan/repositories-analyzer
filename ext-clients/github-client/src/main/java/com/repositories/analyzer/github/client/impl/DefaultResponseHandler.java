package com.repositories.analyzer.github.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repositories.analyzer.github.client.exception.GitHubExternalClientException;
import com.repositories.analyzer.github.client.model.response.AbstractGitHubResponse;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.function.Supplier;

@Component
class DefaultResponseHandler implements ResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultResponseHandler.class);

    private final ObjectMapper objectMapper;

    DefaultResponseHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <R extends AbstractGitHubResponse<T>, T> R handle(final Response response,
                                                             final TypeReference<T> reference,
                                                             final Supplier<R> constructor) {
        Assert.notNull(response, "Null was passed as an argument for parameter 'response'.");
        Assert.notNull(reference, "Null was passed as an argument for parameter 'reference'.");
        Assert.notNull(constructor, "Null was passed as an argument for parameter 'constructor'.");
        final HttpStatus httpStatus = httpStatusFor(response);
        if (httpStatus.is2xxSuccessful()) {
            final R r = constructor.get();
            r.setResponse(responseFrom(response.body(), reference));
            logger.debug("Successfully handled GitHub API response. Response: {}.", r.getResponse());
            return r;
        }
        if (httpStatus.is4xxClientError()) {
            if (httpStatus == HttpStatus.FORBIDDEN) {
                final R r = constructor.get();
                r.setThreshHoldReached(true);
                logger.warn("GitHub API requests limit exceeds.");
                return r;
            }
        }
        logger.error("Unexpected response from github api. Headers: {}", response.headers());
        throw new GitHubExternalClientException(
                String.format("Unexpected response from github api. Http status: %s", httpStatus)
        );
    }

    private static HttpStatus httpStatusFor(final Response response) {
        final HttpStatus httpStatus = HttpStatus.resolve(response.status());
        if (httpStatus == null) {
            throw new GitHubExternalClientException(
                    String.format("Unable to resolve http status with code '%s'.", response.status())
            );
        }
        return httpStatus;
    }

    public <T> T responseFrom(final Response.Body body, final TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(body.asInputStream(), typeReference);
        } catch (final IOException ex) {
            throw new GitHubExternalClientException("Unable to ready body from response.", ex);
        }
    }
}