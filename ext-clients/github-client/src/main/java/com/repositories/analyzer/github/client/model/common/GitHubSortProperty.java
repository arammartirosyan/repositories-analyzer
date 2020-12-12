package com.repositories.analyzer.github.client.model.common;

public enum GitHubSortProperty {

    STARS("stars"), FORKS("forks"), UPDATED("updated");

    private final String value;

    GitHubSortProperty(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}