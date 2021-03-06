package com.repositories.analyzer.service.repository.statistics.commits.refresh;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public interface RefreshRepositoryCommitParameter {

    String name();

    String email();

    String message();

    LocalDate committedOn();

    static RefreshRepositoryCommitParameter of(final String name, final String email, final String message, final LocalDate committedOn) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'name'.");
        }
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'email'.");
        }
        if (StringUtils.isBlank(message)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'message'.");
        }
        if (committedOn == null) {
            throw new IllegalArgumentException("Null was passed as an argument for parameter 'committedOn'.");
        }
        return new ImmutableRefreshRepositoryCommitParameter(name, email, message, committedOn);
    }
}