package com.DavidsCode.CommunityWebPage.config;

import kong.unirest.Headers;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class APIConfig {

    protected final String apiKey;
    protected final String endpoint;
    protected APIConfig(String apiKey, String endpoint) {
        this.apiKey = apiKey;
        this.endpoint = endpoint;
    }

    protected CompletableFuture<String> postRequest(String body) {

        return Unirest.post(endpoint)
                .headers(getHeaders())
                .body(body)
                .asStringAsync()
                .thenApply(response -> response.getBody());
    }

    protected CompletableFuture<String> getRequest(String url) {

        return Unirest.get(url)
                .headers(getHeaders())
                .asStringAsync()
                .thenApply(response -> response.getBody());
    }

    protected abstract Map<String, String> getHeaders();

}