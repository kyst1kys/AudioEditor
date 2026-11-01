package com.nievierov.client.ui;

import com.nievierov.client.service.AudioApiService;
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

    private final AudioApiService audioApiService = new AudioApiService();

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
            String name = nameField.getText().trim();
            String path = pathField.getText().trim();
            String sampleRateStr = sampleRateField.getText().trim();

            if (name.isBlank() || path.isBlank() || sampleRateStr.isBlank()) {
                projectsArea.setText("Помилка: Усі поля повинні бути заповнені!");
                return;
            }

            projectsArea.setText("-> Відправка POST на /projects...\n");

            audioApiService.createProject(name, path, sampleRateStr)
                    .thenAccept(res -> Platform.runLater(() -> projectsArea.setText(res)))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> projectsArea.setText("Помилка: " + ex.getMessage()));
                        return null;
                    });
        });

        getAllProjectsBtn.setOnAction(e -> {
            projectsArea.setText("-> Відправка GET на /projects...\n");

            audioApiService.getAllProjects()
                    .thenAccept(res -> Platform.runLater(() -> projectsArea.setText(res)))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> projectsArea.setText("Помилка: " + ex.getMessage()));
                        return null;
                    });
        });

        // -------------------------------------------------------------
        // ФОРМА 2: Управління доріжками (Audio Tracks)
        // -------------------------------------------------------------
        Tab tracksTab = new Tab("2. Аудіодоріжки (Audio Tracks)");
        tracksTab.setClosable(false);

        TextField projectIdField = new TextField();
        projectIdField.setPromptText("UUID проєкту (напр. 550e8400-e29b-41d4-a716-446655440000)");

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
        tracksGrid.add(createTrackBtn, 1, 1);

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

            tracksArea.setText("-> Відправка POST на /tracks?projectId=" + projId + "...\n");

            audioApiService.createTrack(projId)
                    .thenAccept(res -> Platform.runLater(() -> tracksArea.setText(res)))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> tracksArea.setText("Помилка: " + ex.getMessage()));
                        return null;
                    });

        });

        // Логіка кнопки GET (Отримання доріжок для конкретного проєкту)
        getTracksBtn.setOnAction(e -> {
            String projId = projectIdField.getText().trim();

            // Захист: якщо поле порожнє — НЕ відправляємо запит
            if (projId.isBlank()) {
                tracksArea.setText("Помилка: Спочатку вкажіть UUID проєкту у полі Project UUID!\n");
                return;
            }

            tracksArea.setText("-> Відправка GET на /tracks?projectId=" + projId + "...\n");

            audioApiService.getAllTracks(projId)
                    .thenAccept(res -> Platform.runLater(() -> tracksArea.setText(res)))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> tracksArea.setText("Помилка: " + ex.getMessage()));
                        return null;
                    });
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
}