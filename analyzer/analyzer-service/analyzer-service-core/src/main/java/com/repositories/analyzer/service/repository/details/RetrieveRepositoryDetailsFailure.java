package com.repositories.analyzer.service.repository.details;

import com.repositories.analyzer.common.usecase.Failure;

public enum RetrieveRepositoryDetailsFailure implements Failure {

    REPOSITORY_NOT_FOUND("failure.repository.not.found", "Repository not found.");

    private final String code;

    private final String reason;

    RetrieveRepositoryDetailsFailure(final String code, final String reason) {
        this.code = code;
        this.reason = reason;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String reason() {
        return reason;
    }
}