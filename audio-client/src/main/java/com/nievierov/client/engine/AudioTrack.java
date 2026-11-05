package com.nievierov.client.engine;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AudioTrack {
    private Long id;
    private Double volume = 1.0;
    private Boolean isMuted = false;
    private List<AudioClip> audioClips = new ArrayList<AudioClip>();

    public void addClip(AudioClip audioClip) {
        audioClips.add(audioClip);
    }
}
