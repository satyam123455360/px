package com.uniq.phoenix.search;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleSearch {

    private static final String API_KEY = "AIzaSyCydeMko3fzkQQw8qT0S4QFmVSzmp5vOeo";
    private static final String CX = "61f11537f38fd494c";

    private static int dailySearchCount = 0;
    private static final int DAILY_LIMIT = 85;

    private final HttpClient client;

    public GoogleSearch() {
        this.client = HttpClient.newHttpClient();
    }

    public String search(String query) {

        if (query == null || query.trim().isEmpty()) {
            return "Search query cannot be empty.";
        }

        if (dailySearchCount >= DAILY_LIMIT) {
            return "Google search limit (85/day) reached.";
        }

        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            String url = "https://www.googleapis.com/customsearch/v1"
                    + "?key=" + API_KEY
                    + "&cx=" + CX
                    + "&q=" + encodedQuery;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return "Google API error: " + response.statusCode();
            }

            dailySearchCount++;

            JSONObject json = new JSONObject(response.body());

            if (!json.has("items")) {
                return "No results found.";
            }

            JSONArray items = json.getJSONArray("items");
            JSONObject firstResult = items.getJSONObject(0);

            String title = firstResult.getString("title");
            String snippet = firstResult.getString("snippet");
            String link = firstResult.getString("link");

            return """
                    Title: %s
                    
                    %s
                    
                    Source: %s
                    """.formatted(title, snippet, link);

        } catch (Exception e) {
            return "Error performing search: " + e.getMessage();
        }
    }
}
