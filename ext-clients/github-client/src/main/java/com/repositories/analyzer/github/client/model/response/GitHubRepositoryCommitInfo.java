package com.repositories.analyzer.github.client.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepositoryCommitInfo {

    @JsonProperty("message")
    private String message;

    @JsonProperty("author")
    private GitHubRepositoryCommitter author;

    @JsonProperty("committer")
    private GitHubRepositoryCommitter committer;

    public GitHubRepositoryCommitInfo() {
        super();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public GitHubRepositoryCommitter getAuthor() {
        return author;
    }

    public void setAuthor(final GitHubRepositoryCommitter author) {
        this.author = author;
    }

    public GitHubRepositoryCommitter getCommitter() {
        return committer;
    }

    public void setCommitter(final GitHubRepositoryCommitter committer) {
        this.committer = committer;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitHubRepositoryCommitInfo)) {
            return false;
        }
        final GitHubRepositoryCommitInfo that = (GitHubRepositoryCommitInfo) o;
        return new EqualsBuilder()
                .append(getMessage(), that.getMessage())
                .append(getAuthor(), that.getAuthor())
                .append(getCommitter(), that.getCommitter())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getMessage())
                .append(getAuthor())
                .append(getCommitter())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("message", getMessage())
                .append("author", getAuthor())
                .append("committer", getCommitter())
                .toString();
    }
}