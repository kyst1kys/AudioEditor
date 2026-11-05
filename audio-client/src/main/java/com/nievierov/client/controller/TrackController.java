package com.nievierov.client.controller;

import com.nievierov.client.service.AudioApiService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TrackController {

    @FXML private TextField projectIdField;
    @FXML private TextArea tracksArea;

    private final AudioApiService audioApiService = new AudioApiService();

    @FXML
    private void handleCreateTrack() {
        String projId = projectIdField.getText().trim();
        if (projId.isBlank()) {
            tracksArea.setText("Помилка: Потрібно вказати UUID проєкту!\n");
            return;
        }
        tracksArea.setText("-> Відправка POST на /tracks?projectId=" + projId + "...\n");
        audioApiService.createTrack(projId)
                .thenAccept(res -> Platform.runLater(() -> tracksArea.setText(res)))
                .exceptionally(ex -> {
                    Platform.runLater(() -> tracksArea.setText("Помилка: " + ex.getMessage()));
                    return null;
                });
    }

    @FXML
    private void handleGetTracks() {
        String projId = projectIdField.getText().trim();
        if (projId.isBlank()) {
            tracksArea.setText("Помилка: Спочатку вкажіть UUID проєкту!\n");
            return;
        }
        tracksArea.setText("-> Відправка GET на /tracks?projectId=" + projId + "...\n");
        audioApiService.getAllTracks(projId)
                .thenAccept(res -> Platform.runLater(() -> tracksArea.setText(res)))
                .exceptionally(ex -> {
                    Platform.runLater(() -> tracksArea.setText("Помилка: " + ex.getMessage()));
                    return null;
                });
    }
}