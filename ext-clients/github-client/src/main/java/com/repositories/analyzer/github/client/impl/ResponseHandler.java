package com.repositories.analyzer.github.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.repositories.analyzer.github.client.model.response.AbstractGitHubResponse;
import feign.Response;

import java.util.function.Supplier;

interface ResponseHandler {

    <R extends AbstractGitHubResponse<T>, T> R handle(Response response, TypeReference<T> reference, Supplier<R> constructor);
}
