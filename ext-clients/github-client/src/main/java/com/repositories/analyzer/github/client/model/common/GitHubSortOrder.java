package com.repositories.analyzer.github.client.model.common;

public enum GitHubSortOrder {

    DESC("desc"), ASC("asc");

    private final String value;

    GitHubSortOrder(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}