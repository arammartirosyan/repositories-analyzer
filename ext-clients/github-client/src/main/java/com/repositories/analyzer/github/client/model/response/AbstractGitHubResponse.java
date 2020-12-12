package com.repositories.analyzer.github.client.model.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractGitHubResponse<T> {

    private T response;

    private boolean threshHoldReached;

    public AbstractGitHubResponse() {
        super();
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(final T response) {
        this.response = response;
    }

    public boolean isThreshHoldReached() {
        return threshHoldReached;
    }

    public void setThreshHoldReached(final boolean threshHoldReached) {
        this.threshHoldReached = threshHoldReached;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractGitHubResponse)) {
            return false;
        }
        final AbstractGitHubResponse<?> that = (AbstractGitHubResponse<?>) o;
        return new EqualsBuilder()
                .append(isThreshHoldReached(), that.isThreshHoldReached())
                .append(getResponse(), that.getResponse())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getResponse())
                .append(isThreshHoldReached())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("response", getResponse())
                .append("threshHoldReached", isThreshHoldReached())
                .toString();
    }
}