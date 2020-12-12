package com.repositories.analyzer.github.client.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepositoryContributor {

    @JsonProperty("author")
    private GitHubUser author;

    public GitHubRepositoryContributor() {
        super();
    }

    public GitHubUser getAuthor() {
        return author;
    }

    public void setAuthor(final GitHubUser author) {
        this.author = author;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitHubRepositoryContributor)) {
            return false;
        }
        final GitHubRepositoryContributor that = (GitHubRepositoryContributor) o;
        return new EqualsBuilder()
                .append(getAuthor(), that.getAuthor())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAuthor())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("author", getAuthor())
                .toString();
    }
}