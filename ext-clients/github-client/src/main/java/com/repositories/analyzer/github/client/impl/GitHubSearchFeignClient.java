package com.repositories.analyzer.github.client.impl;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gitHubSearchFeignClient", path = "search", url = "${github.api.url}")
interface GitHubSearchFeignClient {

    @GetMapping("repositories")
    Response search(@RequestParam("q") String query);

    @GetMapping("repositories")
    Response search(@RequestParam("q") String query, @RequestParam("sort") String sort, @RequestParam("order") String order);
}