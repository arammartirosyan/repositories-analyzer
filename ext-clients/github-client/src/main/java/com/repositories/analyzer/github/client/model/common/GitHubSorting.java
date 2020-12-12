package com.repositories.analyzer.github.client.model.common;

public interface GitHubSorting {

    GitHubSortOrder order();

    GitHubSortProperty property();

    static GitHubSorting of(final GitHubSortOrder order, final GitHubSortProperty property) {
        if (order == null) {
            throw new IllegalArgumentException("Null was passed as an argument for parameter 'order'.");
        }
        if (property == null) {
            throw new IllegalArgumentException("Null was passed as an argument for parameter 'property'.");
        }
        return new ImmutableGitHubSorting(order, property);
    }
}