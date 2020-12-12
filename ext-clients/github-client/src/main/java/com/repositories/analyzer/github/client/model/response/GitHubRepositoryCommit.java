package com.repositories.analyzer.github.client.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepositoryCommit {

    @JsonProperty("author")
    private GitHubUser author;

    @JsonProperty("committer")
    private GitHubUser committer;

    @JsonProperty("commit")
    private GitHubRepositoryCommitInfo commitInfo;

    public GitHubRepositoryCommit() {
        super();
    }

    public GitHubUser getAuthor() {
        return author;
    }

    public void setAuthor(final GitHubUser author) {
        this.author = author;
    }

    public GitHubUser getCommitter() {
        return committer;
    }

    public void setCommitter(final GitHubUser committer) {
        this.committer = committer;
    }

    public GitHubRepositoryCommitInfo getCommitInfo() {
        return commitInfo;
    }

    public void setCommitInfo(final GitHubRepositoryCommitInfo commitInfo) {
        this.commitInfo = commitInfo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitHubRepositoryCommit)) {
            return false;
        }
        final GitHubRepositoryCommit that = (GitHubRepositoryCommit) o;
        return new EqualsBuilder()
                .append(getAuthor(), that.getAuthor())
                .append(getCommitter(), that.getCommitter())
                .append(getCommitInfo(), that.getCommitInfo())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAuthor())
                .append(getCommitter())
                .append(getCommitInfo())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("author", getAuthor())
                .append("committer", getCommitter())
                .append("commitInfo", getCommitInfo())
                .toString();
    }
}