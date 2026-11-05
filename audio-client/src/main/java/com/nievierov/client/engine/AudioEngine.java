package com.nievierov.client.engine;

import lombok.Getter;

import javax.sound.sampled.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioEngine {
    private static final AudioEngine INSTANCE = new AudioEngine();

    private final ExecutorService audioExecutor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r,  "Audio-Playback-Thread");
                t.setDaemon(true);
                t.setPriority(Thread.MAX_PRIORITY);
                return t;
            }
    );

    @Getter
    private volatile boolean isPlaying = false;

    private SourceDataLine audioLine;

    private AudioEngine() {
        System.out.println("[AudioEngine] Singleton-екземпляр ініціалізовано.");
    }

    public static AudioEngine getInstance() {
        return INSTANCE;
    }

    // в майбутньому будемо передавати трек, утворений аудіомікшером
    public void play(AudioTrack audioTrack) {
        if (isPlaying) {
            stop();
        }

        if (audioTrack.getAudioClips().isEmpty()) {
            System.out.println("[AudioEngine] На доріжці немає кліпів для відтворення.");
            return;
        }

        isPlaying = true;
        audioExecutor.submit(() -> runPlayback(audioTrack));
    }

    public void stop() {
        isPlaying = false;
        if (audioLine != null) {
            audioLine.stop();
            audioLine.close();
        }
    }

    public void runPlayback(AudioTrack audioTrack) {
        System.out.println("[AudioEngine] Старт відтворення доріжки ID: " + audioTrack.getId());

        byte[] buffer = new byte[4096];
        byte[] silenceBuffer = new byte[4096];
        Arrays.fill(silenceBuffer, (byte) 0); // Заповнюємо буфер тиші нулями

        for (AudioClip audioClip : audioTrack.getAudioClips()) {
            if (!isPlaying) {
                break; // Якщо натиснули stop(), виходимо з циклу кліпів
            }

            System.out.println("[AudioEngine] Відтворення кліпу: " + audioClip.getSourceFilePath());

            try (AudioInputStream stream = audioClip.getAudioAdapter().getAudioInputStream()) {
                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                audioLine = (SourceDataLine) AudioSystem.getLine(info);
                // Вказуємо ОС виділити буфер приблизно на 8192 байти, щоб зменшити затримку після кнопки Mute
                int systemBufferSize = 8192;
                audioLine.open(format, systemBufferSize);
                audioLine.start();

                int bytesRead;

                while (isPlaying && (bytesRead = stream.read(buffer, 0, buffer.length)) != -1) {
                    if (Boolean.TRUE.equals(audioTrack.getIsMuted())) {
                        audioLine.write(silenceBuffer, 0, bytesRead);
                    } else {
                        audioLine.write(buffer, 0, bytesRead);
                    }
                }

                if (isPlaying) {
                    audioLine.drain();
                }

                audioLine.stop();
                audioLine.close();

            } catch (Exception e) {
                System.err.println("[AudioEngine] Помилка під час відтворення кліпу: " + e.getMessage());
                isPlaying = false;
            }
        }

        // Зупиняємо загальний статус тільки після того, як програли всі кліпи
        if (isPlaying) {
            isPlaying = false;
            System.out.println("[AudioEngine] Відтворення всіх кліпів на доріжці завершено.");
        }
    }
}