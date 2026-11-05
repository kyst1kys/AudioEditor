package com.nievierov.client.controller;

import com.nievierov.client.service.AudioApiService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ProjectController {

    @FXML private TextField nameField;
    @FXML private TextField pathField;
    @FXML private TextField sampleRateField;
    @FXML private TextArea projectsArea;

    private final AudioApiService audioApiService = new AudioApiService();

    @FXML
    private void handleCreateProject() {
        String name = nameField.getText().trim();
        String path = pathField.getText().trim();
        String sampleRate = sampleRateField.getText().trim();

        if (name.isBlank() || path.isBlank() || sampleRate.isBlank()) {
            projectsArea.setText("Помилка: Усі поля повинні бути заповнені!");
            return;
        }

        projectsArea.setText("-> Відправка POST на /projects...\n");
        audioApiService.createProject(name, path, sampleRate)
                .thenAccept(res -> Platform.runLater(() -> projectsArea.setText(res)))
                .exceptionally(ex -> {
                    Platform.runLater(() -> projectsArea.setText("Помилка: " + ex.getMessage()));
                    return null;
                });
    }

    @FXML
    private void handleGetAllProjects() {
        projectsArea.setText("-> Відправка GET на /projects...\n");
        audioApiService.getAllProjects()
                .thenAccept(res -> Platform.runLater(() -> projectsArea.setText(res)))
                .exceptionally(ex -> {
                    Platform.runLater(() -> projectsArea.setText("Помилка: " + ex.getMessage()));
                    return null;
                });
    }
}