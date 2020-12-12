package com.repositories.analyzer.common.api.model;

import com.repositories.analyzer.common.usecase.FailureDto;

import java.util.List;

public interface Response {

    default List<FailureDto> getFailures() {
        return List.of();
    }

    default boolean isSuccessful() {
        final List<FailureDto> failures = getFailures();
        return failures == null || failures.isEmpty();
    }
}