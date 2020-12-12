package com.repositories.analyzer.common.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.repositories.analyzer.common.usecase.Failure;
import com.repositories.analyzer.common.usecase.FailureDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractResponse implements Response {

    @JsonProperty
    private List<FailureDto> failures;

    public AbstractResponse() {
        super();
    }

    public AbstractResponse(final List<FailureDto> failures) {
        this.failures = failures;
    }

    public AbstractResponse(final Collection<? extends Failure> failures) {
        this.failures = toFailureDtoList(failures);
    }

    @JsonProperty
    @Override
    public boolean isSuccessful() {
        return !isFailed();
    }

    @JsonIgnore
    public boolean isFailed() {
        return failures != null && !failures.isEmpty();
    }

    @Override
    public List<FailureDto> getFailures() {
        return failures;
    }

    public void setFailures(final List<FailureDto> failures) {
        this.failures = failures;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractResponse)) {
            return false;
        }
        final AbstractResponse that = (AbstractResponse) o;
        return new EqualsBuilder()
                .append(getFailures(), that.getFailures())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getFailures())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("failures", getFailures())
                .append("successful", isSuccessful())
                .append("failed", isFailed())
                .toString();
    }

    private static List<FailureDto> toFailureDtoList(final Collection<? extends Failure> failures) {
        return failures.stream()
                .map(failure -> new FailureDto(failure.code(), failure.reason()))
                .collect(Collectors.toUnmodifiableList());
    }
}