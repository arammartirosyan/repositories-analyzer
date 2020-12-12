package com.repositories.analyzer.persistence.statistics.contributors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

public class RepositoryContributor {

    @Field(name = "remote_id")
    private String remoteId;

    @Field(name = "login")
    private String login;

    @Field(name = "type")
    private String type;

    /* Will be used by reflection */
    protected RepositoryContributor() {
        super();
    }

    public RepositoryContributor(final String remoteId, final String login, final String type) {
        this.remoteId = remoteId;
        this.login = login;
        this.type = type;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public String getLogin() {
        return login;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("remoteId", getRemoteId())
                .append("login", getLogin())
                .append("type", getType())
                .toString();
    }
}