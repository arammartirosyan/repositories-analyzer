package com.repositories.analyzer.service.repository.statistics.commits;

import com.repositories.analyzer.common.usecase.Failure;

public enum RetrieveRepositoryCommitsFailure implements Failure {

    ;

    private final String code;

    private final String reason;

    RetrieveRepositoryCommitsFailure(final String code, final String reason) {
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