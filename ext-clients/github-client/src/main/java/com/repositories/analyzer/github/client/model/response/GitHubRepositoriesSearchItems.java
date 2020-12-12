package com.repositories.analyzer.github.client.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class GitHubRepositoriesSearchItems {

    @JsonProperty("items")
    private List<GitHubRepository> items;

    public GitHubRepositoriesSearchItems() {
        super();
    }

    public List<GitHubRepository> getItems() {
        return items;
    }

    public void setItems(final List<GitHubRepository> items) {
        this.items = items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitHubRepositoriesSearchItems)) {
            return false;
        }
        final GitHubRepositoriesSearchItems that = (GitHubRepositoriesSearchItems) o;
        return new EqualsBuilder()
                .append(getItems(), that.getItems())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getItems())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("items", getItems())
                .toString();
    }
}