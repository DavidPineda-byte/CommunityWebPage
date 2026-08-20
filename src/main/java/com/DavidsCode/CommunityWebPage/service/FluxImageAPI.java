package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.config.APIConfig;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class FluxImageAPI extends APIConfig {

    public String getImageURl(String Prompt) {

        return "image URL";
    }


    public FluxImageAPI() {
        super(System.getenv("FLUX_API_KEY"), "https://api.bfl.ai/v1/flux-dev");
    }

    @Override
    protected Map<String, String> getHeaders() {
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("ERROR: FLUX_API_KEY environment variable is not set or is empty!");
        }
        return Map.of("x-key", apiKey != null ? apiKey : "", "Content-Type", "application/json");
    }

    public CompletableFuture<String> generateImageURL(String prompt) {

        String body = """
                {
                  "prompt": "%s",
                  "seed": 42,
                  "width": 1024,
                  "height": 768,
                  "safety_tolerance": 2,
                  "output_format": "jpeg"
                }
                """.formatted(prompt);


        return postRequest(body)
                .thenCompose(this::imageUrlParser);
    }

    public CompletableFuture<String> getTaskResponse(String pollingUrl) {

        return getRequest(pollingUrl).thenCompose(response -> {

            System.out.println("Polling response: " + response);

            JSONObject responseBody = new JSONObject(response);

            String status = responseBody.getString("status");

            if (status.equals("Pending") || status.equals("Task not found")) {

                return CompletableFuture.supplyAsync(() -> null, CompletableFuture.delayedExecutor(2, java.util.concurrent.TimeUnit.SECONDS)).thenCompose(v -> getTaskResponse(pollingUrl));
            }

            JSONObject result = responseBody.getJSONObject("result");

            String imageUrl = result.getString("sample");

            return CompletableFuture.completedFuture(imageUrl);
        });
    }


    public CompletableFuture<String> imageUrlParser(String response) {

        System.out.println("response: " + response);

        JSONObject responseJson = new JSONObject(response);

        if (!responseJson.has("polling_url")) {
            String errorMsg = responseJson.has("detail") ? responseJson.getString("detail") : "Unknown API error";
            System.err.println("API Error: " + errorMsg);
            return CompletableFuture.completedFuture("ERROR: " + errorMsg);
        }

        String pollingUrl = responseJson.getString("polling_url");

        return getTaskResponse(pollingUrl).thenApply(imageUrl -> {

            System.out.println(imageUrl);
            return imageUrl;
        });
    }

}
