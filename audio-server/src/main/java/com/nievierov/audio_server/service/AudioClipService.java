package com.nievierov.audio_server.service;

import com.nievierov.audio_server.entity.AudioClip;
import com.nievierov.audio_server.repository.AudioClipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioClipService {
    private final AudioClipRepository audioClipRepository;

    public AudioClip getAudioClipById(Long id) {
        return audioClipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AudioClip not found"));
    }

    public List<AudioClip> getAllAudioClips(Long audioTrackId) {
        return audioClipRepository.findAllByAudioTrackId(audioTrackId);
    }


}
