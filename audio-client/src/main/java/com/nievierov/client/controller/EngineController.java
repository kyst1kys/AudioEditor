package com.nievierov.client.controller;

import com.nievierov.client.engine.*;
import com.nievierov.client.service.AudioApiService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.*;

import java.io.File;

public class EngineController {

    @FXML private TextField trackIdInput;
    @FXML private CheckBox muteCheckBox;
    @FXML private Button playBtn;
    @FXML private Button stopBtn;
    @FXML private Label engineStatusLabel;
    @FXML private TextArea clipsArea;

    private final AudioApiService audioApiService = new AudioApiService();
    private final AudioTrack currentLocalTrack = new AudioTrack();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    private void handleLoadTrack() {
        String trackId = trackIdInput.getText().trim();
        if (trackId.isBlank()) {
            clipsArea.setText("Помилка: Вкажіть ID доріжки у полі Track ID!\n");
            return;
        }

        clipsArea.setText("-> Відправка GET на /clips?trackId=" + trackId + "...\n");
        audioApiService.getClipsByTrack(trackId)
                .thenAccept(res -> Platform.runLater(() -> {
                    clipsArea.setText(res);
                    try {
                        currentLocalTrack.setId(Long.parseLong(trackId));
                        currentLocalTrack.getAudioClips().clear();
                        parseAndFillClips(res);
                        engineStatusLabel.setText("Успішно завантажено! Кліпів у рушії: " + currentLocalTrack.getAudioClips().size());
                    } catch (Exception ex) {
                        engineStatusLabel.setText("Помилка розбору JSON: " + ex.getMessage());
                    }
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> clipsArea.setText("Помилка: " + ex.getMessage()));
                    return null;
                });
    }

    @FXML
    private void handleAddClip() {
        String trackId = trackIdInput.getText().trim();
        if (trackId.isBlank()) {
            engineStatusLabel.setText("Помилка: Спочатку вкажіть Track ID!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Оберіть WAV файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV Audio (*.wav)", "*.wav"));

        Stage stage = (Stage) clipsArea.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                AudioClip clip = new AudioClip(selectedFile.getAbsolutePath());
                currentLocalTrack.addClip(clip);

                engineStatusLabel.setText("-> Відправка POST на /clips?trackId=" + trackId + "...");

                audioApiService.createClip(trackId, selectedFile.getAbsolutePath(), clip.getDurationMs())
                        .thenAccept(res -> Platform.runLater(() -> {
                            clipsArea.setText("Відповідь сервера (POST):\n" + res);
                            engineStatusLabel.setText("Успішно збережено! Кліпів у локальному рушії: " + currentLocalTrack.getAudioClips().size());
                        }))
                        .exceptionally(ex -> {
                            Platform.runLater(() -> engineStatusLabel.setText("Помилка: " + ex.getMessage()));
                            return null;
                        });

            } catch (Exception ex) {
                engineStatusLabel.setText("Помилка відкриття файлу: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void handleMuteToggle() {


        currentLocalTrack.setIsMuted(muteCheckBox.isSelected());
    }

    @FXML
    private void handlePlay() {
        if (currentLocalTrack.getAudioClips().isEmpty()) {
            engineStatusLabel.setText("Помилка: Додайте хоча б один WAV кліп!");
            return;
        }

        AudioEngine.getInstance().play(currentLocalTrack);
        engineStatusLabel.setText("▶ Відтворення локальних кліпів...");
        playBtn.setDisable(true);
        stopBtn.setDisable(false);

        new Thread(() -> {
            while (AudioEngine.getInstance().isPlaying()) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
            Platform.runLater(() -> {
                engineStatusLabel.setText("⏹ Відтворення завершено");
                playBtn.setDisable(false);
                stopBtn.setDisable(true);
            });
        }).start();
    }

    @FXML
    private void handleStop() {
        AudioEngine.getInstance().stop();
        engineStatusLabel.setText("⏹ Відтворення зупинено");
        playBtn.setDisable(false);
        stopBtn.setDisable(true);
    }

    private void parseAndFillClips(String json) throws Exception {
        if (json == null || json.isBlank()) return;

        // Шукаємо де реально починається JSON масив '[' або об'єкт '{'
        int jsonStart = json.indexOf("[");
        if (jsonStart == -1) {
            jsonStart = json.indexOf("{");
        }

        // Якщо JSON-структури немає взагалі — значить там прийшла помилка HTTP
        if (jsonStart == -1) {
            System.err.println("Сервер повернув не JSON: " + json);
            return;
        }

        // Відрізаємо HTTP-заголовки і залишаємо тільки чистий JSON
        String cleanJson = json.substring(jsonStart);

        JsonNode rootNode = objectMapper.readTree(cleanJson);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                String filePath = node.path("sourceFilePath").asText();
                if (!filePath.isBlank() && new File(filePath).exists()) {
                    currentLocalTrack.addClip(new AudioClip(filePath));
                }
            }
        }
    }
}