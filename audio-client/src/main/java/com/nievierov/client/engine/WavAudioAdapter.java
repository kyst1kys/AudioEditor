package com.nievierov.client.engine;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class WavAudioAdapter {
    private final File audioFile;

    public WavAudioAdapter(String filePath) {
        this.audioFile = new File(filePath);
        if (!audioFile.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }
    }

    public AudioInputStream getAudioInputStream() throws Exception {
        return AudioSystem.getAudioInputStream(audioFile);
    }
}
