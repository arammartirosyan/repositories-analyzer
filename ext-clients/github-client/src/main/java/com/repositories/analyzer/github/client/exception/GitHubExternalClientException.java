package com.repositories.analyzer.github.client.exception;

public class GitHubExternalClientException extends RuntimeException {

    public GitHubExternalClientException(final String message) {
        super(message);
    }

    public GitHubExternalClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}