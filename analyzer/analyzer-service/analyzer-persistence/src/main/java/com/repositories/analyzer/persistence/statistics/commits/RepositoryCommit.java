package com.repositories.analyzer.persistence.statistics.commits;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class RepositoryCommit {

    @Field(name = "name")
    private String name;

    @Field(name = "email")
    private String email;

    @Field(name = "message")
    private String message;

    @Field(name = "committed_on")
    private LocalDate committedOn;

    /* Will be used by reflection */
    protected RepositoryCommit() {
        super();
    }

    public RepositoryCommit(final String name, final String email, final String message, final LocalDate committedOn) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.committedOn = committedOn;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getCommittedOn() {
        return committedOn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", getName())
                .append("email", getEmail())
                .append("message", getMessage())
                .append("committedOn", getCommittedOn())
                .toString();
    }
}