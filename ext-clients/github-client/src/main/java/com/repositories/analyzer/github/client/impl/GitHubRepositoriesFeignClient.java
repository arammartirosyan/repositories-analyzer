package com.repositories.analyzer.github.client.impl;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gitHubRepositoriesFeignClient", path = "repos", url = "${github.api.url}")
interface GitHubRepositoriesFeignClient {

    @GetMapping("{fullName}/stats/contributors")
    Response retrieveContributors(@PathVariable("fullName") String fullName);

    @GetMapping("{fullName}/commits")
    Response retrieveCommits(@PathVariable("fullName") String fullName, @RequestParam("per_page") int perPage);
}