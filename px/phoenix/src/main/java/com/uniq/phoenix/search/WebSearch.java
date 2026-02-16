package com.uniq.phoenix.search;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class WebSearch {

    private final HttpClient client;

    public WebSearch() {
        this.client = HttpClient.newHttpClient();
    }

    public String search(String query) {

        // 1️⃣ Validate input
        if (query == null || query.trim().isEmpty()) {
            return "Search query cannot be empty.";
        }

        try {
            // 2️⃣ Format query
            String formattedQuery = query.trim();
            formattedQuery = URLEncoder.encode(formattedQuery, StandardCharsets.UTF_8);

            // 3️⃣ Wikipedia REST API
            String url = "https://en.wikipedia.org/api/rest_v1/page/summary/" + formattedQuery;

            // 4️⃣ Create request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("User-Agent", "Phoenix-AI/1.0")
                    .build();

            // 5️⃣ Send request
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // 6️⃣ Check response
            if (response.statusCode() != 200) {
                return "No results found on Wikipedia.";
            }

            // 7️⃣ Extract summary manually (v0 simple JSON parsing)
            String body = response.body();

            String marker = "\"extract\":\"";
            int start = body.indexOf(marker);

            if (start == -1) {
                return "No summary available.";
            }

            start += marker.length();
            int end = body.indexOf("\",", start);

            if (end == -1) {
                return "No summary available.";
            }

            String summary = body.substring(start, end);

            // Replace escaped characters
            summary = summary.replace("\\n", "\n");
            summary = summary.replace("\\\"", "\"");

            return summary;

        } catch (Exception e) {
            return "Error occurred while searching: " + e.getMessage();
        }
    }
}

