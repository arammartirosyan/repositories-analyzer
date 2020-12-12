package com.repositories.analyzer.common.usecase;

public enum CommonFailures implements Failure {

    INTERNAL_SERVER_ERROR("failure.internal server.error", "Internal server error."),
    API_THRESHHOLD_REACHED_FAILURE("failure.api.threshhold.reached", "API threshhold limit reached, please try a bit later.");

    private final String code;

    private final String reason;

    CommonFailures(final String code, final String reason) {
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