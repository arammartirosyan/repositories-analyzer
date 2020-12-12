package com.repositories.analyzer.service.repository.statistics.contributors;

import com.repositories.analyzer.common.usecase.Failure;

public enum RetrieveRepositoryContributorsFailure implements Failure {

    ;

    private final String code;

    private final String reason;

    RetrieveRepositoryContributorsFailure(final String code, final String reason) {
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