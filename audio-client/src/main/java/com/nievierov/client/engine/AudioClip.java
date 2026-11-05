package com.nievierov.client.engine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

@NoArgsConstructor
@Getter @Setter
public class AudioClip {
    private Long id;

    private String sourceFilePath;

    private Long startTimeMs = 0L;

    private Long durationMs = 0L;

    private Long startOffsetMs = 0L;

    private WavAudioAdapter audioAdapter;

    public AudioClip(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
        this.audioAdapter = new WavAudioAdapter(sourceFilePath);
        this.durationMs = calculateDurationMs();
    }

    private Long calculateDurationMs() {
        try (AudioInputStream stream = audioAdapter.getAudioInputStream()) {
            AudioFormat format = stream.getFormat();
            long frames = stream.getFrameLength();
            double durationInSeconds = (double) frames / format.getFrameRate();
            return (long) (durationInSeconds * 1000);

        } catch (Exception e) {
            System.err.println("[AudioClip] Помилка під час створення сегменту: " + e.getMessage());
            return 0L;
        }
    }
}
