package com.nievierov.client.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AudioApiService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String BASE_URL = "http://localhost:8080/app/v1";

    public CompletableFuture<String> createProject(String name, String folderPath, String sampleRate) {
        String jsonBody = """
                    {
                      "name": "%s",
                      "folderPath": "%s",
                      "sampleRate": "%d"
                    }
                    """.formatted(escapeJson(name), escapeJson(folderPath), Integer.parseInt(sampleRate));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/projects"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return sendAsync(request);
    }

    public CompletableFuture<String> getAllProjects() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/projects"))
                .GET()
                .build();
        return sendAsync(request);
    }

    public CompletableFuture<String> createTrack(String projectId) {
        String url = BASE_URL + "/tracks?projectId=" + projectId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return sendAsync(request);
    }

    public CompletableFuture<String> getAllTracks(String projectId) {
        String url = BASE_URL + "/tracks?projectId=" + projectId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return sendAsync(request);
    }

    private CompletableFuture<String> sendAsync(HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> "HTTP " + response.statusCode() + " OK:\n" + response.body());
    }

    private String escapeJson(String input) {
        return input == null ? "" : input.replace("\"", "\\\"");
    }
}
