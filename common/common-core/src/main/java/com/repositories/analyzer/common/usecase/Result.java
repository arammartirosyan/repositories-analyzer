package com.repositories.analyzer.common.usecase;

import java.util.Collection;

public interface Result<F extends Failure> {

    Collection<? extends F> failures();

    default boolean hasFailures() {
        final Collection<? extends Failure> thisFailures = failures();
        return thisFailures != null && !thisFailures.isEmpty();
    }

    default boolean hasFailure(final F failure) {
        final Collection<? extends Failure> thisFailures = failures();
        return thisFailures != null && thisFailures.stream().anyMatch(failure::equals);
    }
}