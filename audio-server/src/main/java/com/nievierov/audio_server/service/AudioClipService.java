package com.nievierov.audio_server.service;

import com.nievierov.audio_server.dto.AudioClipDto;
import com.nievierov.audio_server.dto.AudioClipUpdatedDto;
import com.nievierov.audio_server.entity.AudioClip;
import com.nievierov.audio_server.entity.AudioTrack;
import com.nievierov.audio_server.mapper.AudioClipMapper;
import com.nievierov.audio_server.repository.AudioClipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioClipService {
    private final AudioClipRepository audioClipRepository;
    private final AudioTrackService audioTrackService;
    private final AudioClipMapper audioClipMapper;

    public AudioClip getAudioClipById(Long id) {
        return audioClipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AudioClip not found"));
    }

    public List<AudioClip> getAllAudioClips(Long audioTrackId) {
        return audioClipRepository.findAllByAudioTrackId(audioTrackId);
    }

    public AudioClip addAudioClip(AudioClipDto audioClipDto) {
        AudioTrack audioTrack = audioTrackService.getAudioTrackById(audioClipDto.trackId());

        var audioClip = audioClipMapper.toEntity(audioClipDto);
        audioClip.setAudioTrack(audioTrack);

        return audioClipRepository.save(audioClip);
    }

    public AudioClip updateAudioClip(Long clipId, AudioClipUpdatedDto updatedDto) {
        AudioClip audioClip = getAudioClipById(clipId);

        audioClipMapper.updateEntityFromDto(updatedDto, audioClip);

        if (updatedDto.trackId() != null) {
            var newTrack = audioTrackService.getAudioTrackById(updatedDto.trackId());
            audioClip.setAudioTrack(newTrack);
        }

        return audioClipRepository.save(audioClip);
    }

    @Transactional
    public List<AudioClip> syncAudioClips(List<AudioClipDto> audioClipDtos) {
        List<AudioClip> savedClips = new ArrayList<>();

        for (AudioClipDto dto : audioClipDtos) {
            if (dto.id() != null) {
                AudioClip existingClip = getAudioClipById(dto.id());

                audioClipMapper.updateEntityFromDto(dto, existingClip);

                savedClips.add(audioClipRepository.save(existingClip));
            } else {
                AudioClip newClip = audioClipMapper.toEntity(dto);

                AudioTrack track = audioTrackService.getAudioTrackById(dto.trackId());
                newClip.setAudioTrack(track);

                savedClips.add(audioClipRepository.save(newClip));
            }
        }

        return savedClips;
    }

    public void deleteAudioClip(Long clipId) {
        audioClipRepository.deleteById(clipId);
    }
}
