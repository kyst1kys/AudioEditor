package com.nievierov.audio_client.audioclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloApplication extends Application {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String BASE_URL = "http://localhost:8080/app/v1";

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        // -------------------------------------------------------------
        // ФОРМА 1: Управління проєктами (Створення та перегляд з БД)
        // -------------------------------------------------------------
        Tab projectsTab = new Tab("1. Проєкти (Projects)");
        projectsTab.setClosable(false);

        TextField nameField = new TextField();
        nameField.setPromptText("Назва проєкту");
        TextField pathField = new TextField("D:/TestDAWProject");
        pathField.setPromptText("Шлях на диску");
        TextField sampleRateField = new TextField();
        sampleRateField.setPromptText("Sample rate");

        Button createProjectBtn = new Button("Створити проєкт (POST)");
        Button getAllProjectsBtn = new Button("Завантажити проєкти з БД (GET)");

        TextArea projectsArea = new TextArea();
        projectsArea.setEditable(false);
        projectsArea.setPrefHeight(200);

        GridPane projectGrid = new GridPane();
        projectGrid.setHgap(10);
        projectGrid.setVgap(10);
        projectGrid.add(new Label("Назва:"), 0, 0);
        projectGrid.add(nameField, 1, 0);
        projectGrid.add(new Label("Шлях:"), 0, 2);
        projectGrid.add(pathField, 1, 2);
        projectGrid.add(new Label("Sample rate:"), 0, 3);
        projectGrid.add(sampleRateField, 1, 3);
        projectGrid.add(createProjectBtn, 1, 4);

        VBox projectsLayout = new VBox(10, projectGrid, getAllProjectsBtn, projectsArea);
        projectsLayout.setPadding(new Insets(15));
        projectsTab.setContent(projectsLayout);

        // Логіка кнопок Форми 1
        createProjectBtn.setOnAction(e -> {
            String jsonBody = """
                    {
                      "name": "%s",
                      "folderPath": "%s",
                      "sampleRate": "%s"
                    }
                    """.formatted(nameField.getText(), pathField.getText(), sampleRateField.getText());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/projects"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            sendRequest(request, projectsArea);
        });

        getAllProjectsBtn.setOnAction(e -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/projects"))
                    .GET()
                    .build();
            sendRequest(request, projectsArea);
        });

        // -------------------------------------------------------------
        // ФОРМА 2: Управління доріжками (Audio Tracks)
        // -------------------------------------------------------------
        Tab tracksTab = new Tab("2. Аудіодоріжки (Audio Tracks)");
        tracksTab.setClosable(false);

        TextField projectIdField = new TextField();
        projectIdField.setPromptText("UUID проєкту (напр. 550e8400-e29b-41d4-a716-446655440000)");

        TextField volumeField = new TextField("1.0");
        volumeField.setPromptText("Гучність (0.0 - 1.0)");

        CheckBox mutedCheckBox = new CheckBox("Заглушити (isMuted)");

        Button createTrackBtn = new Button("Створити доріжку (POST)");
        Button getTracksBtn = new Button("Отримати доріжки проєкту (GET)");

        TextArea tracksArea = new TextArea();
        tracksArea.setEditable(false);
        tracksArea.setPrefHeight(200);

        GridPane tracksGrid = new GridPane();
        tracksGrid.setHgap(10);
        tracksGrid.setVgap(10);
        tracksGrid.add(new Label("Project UUID:"), 0, 0);
        tracksGrid.add(projectIdField, 1, 0);
        tracksGrid.add(new Label("Гучність (volume):"), 0, 1);
        tracksGrid.add(volumeField, 1, 1);
        tracksGrid.add(mutedCheckBox, 1, 2);
        tracksGrid.add(createTrackBtn, 1, 3);

        VBox tracksLayout = new VBox(10, tracksGrid, getTracksBtn, tracksArea);
        tracksLayout.setPadding(new Insets(15));
        tracksTab.setContent(tracksLayout);

        // Логіка кнопки POST (Створення доріжки)
        createTrackBtn.setOnAction(e -> {
            String projId = projectIdField.getText().trim();

            if (projId.isBlank()) {
                tracksArea.appendText("Помилка: Потрібно вказати UUID проєкту!\n");
                return;
            }

            double volume = 1.0;
            try {
                if (!volumeField.getText().isBlank()) {
                    volume = Double.parseDouble(volumeField.getText().trim());
                }
            } catch (NumberFormatException ex) {
                tracksArea.appendText("Помилка: Volume має бути дробовим числом (наприклад, 0.8)!\n");
                return;
            }

            String jsonBody = """
            {
              "projectId": "%s",
              "volume": %s,
              "isMuted": %b
            }
            """.formatted(projId, String.valueOf(volume), mutedCheckBox.isSelected());

            String url = BASE_URL + "/tracks?projectId=" + projId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url)) // або твоя адреса ендпоінту доріжок
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            sendRequest(request, tracksArea);
        });

        // Логіка кнопки GET (Отримання доріжок для конкретного проєкту)
        getTracksBtn.setOnAction(e -> {
            String projId = projectIdField.getText().trim();
            String url = BASE_URL + "/tracks?projectId=" + projId;
            // Захист: якщо поле порожнє — НЕ відправляємо запит
            if (projId.isBlank()) {
                tracksArea.setText("Помилка: Спочатку вкажіть UUID проєкту у полі Project UUID!\n");
                return;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            sendRequest(request, tracksArea);
        });
        // -------------------------------------------------------------
        // Збирання головного вікна
        // -------------------------------------------------------------
        tabPane.getTabs().addAll(projectsTab, tracksTab);
        Scene scene = new Scene(tabPane, 550, 480);
        stage.setTitle("DAW Client — Повний цикл роботи з БД");
        stage.setScene(scene);
        stage.show();
    }

    private void sendRequest(HttpRequest request, TextArea console) {
        console.appendText("-> Відправка " + request.method() + " на " + request.uri() + "\n");
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> Platform.runLater(() ->
                        console.appendText("HTTP " + response.statusCode() + " OK:\n" + response.body() + "\n\n")
                ))
                .exceptionally(ex -> {
                    Platform.runLater(() -> console.appendText("Помилка: " + ex.getMessage() + "\n\n"));
                    return null;
                });
    }
}