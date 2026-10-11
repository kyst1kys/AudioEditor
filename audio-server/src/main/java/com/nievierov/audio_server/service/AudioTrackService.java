package com.nievierov.audio_server.service;

import com.nievierov.audio_server.dto.AudioTrackUpdatedDto;
import com.nievierov.audio_server.entity.AudioTrack;
import com.nievierov.audio_server.repository.AudioTrackRepository;
import com.nievierov.audio_server.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudioTrackService {
    private final AudioTrackRepository audioTrackRepository;
    private final ProjectRepository projectRepository;

    public AudioTrack getAudioTrackById(Long id) {
        return audioTrackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AudioTrack Not Found"));
    }

    public List<AudioTrack> getAllAudioTracks(UUID projectId) {
        return audioTrackRepository.findAllByProjectId(projectId);
    }

    public AudioTrack addAudioTrack(UUID projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project Not Found"));
        var audioTrack = new AudioTrack(project);

        return audioTrackRepository.save(audioTrack);
    }

    public AudioTrack updateAudioTrack(Long id, AudioTrackUpdatedDto updatedDto) {
        AudioTrack audioTrack = getAudioTrackById(id);

        if (updatedDto.volume() != null) {
            audioTrack.setVolume(updatedDto.volume());
        }
        if (updatedDto.isMuted() != null) {
            audioTrack.setIsMuted(updatedDto.isMuted());
        }

        return audioTrackRepository.save(audioTrack);
    }

    public void deleteAudioTrackById(Long id) {
        audioTrackRepository.deleteById(id);
    }
}
