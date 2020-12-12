package com.repositories.analyzer.common.usecase;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

public abstract class AbstractResult<F extends Failure> implements Result<F> {

    private final Collection<? extends F> failures;

    public AbstractResult(final Collection<? extends F> failures) {
        this.failures = failures;
    }

    @Override
    public Collection<? extends F> failures() {
        return failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        final Result<?> that = (Result<?>) o;
        return new EqualsBuilder()
                .append(failures(), that.failures())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(failures())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("failures", failures())
                .toString();
    }
}