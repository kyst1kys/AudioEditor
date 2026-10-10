package com.nievierov.audio_server.controller;

import com.nievierov.audio_server.dto.AudioTrackDto;
import com.nievierov.audio_server.dto.AudioTrackUpdatedDto;
import com.nievierov.audio_server.entity.AudioTrack;
import com.nievierov.audio_server.mapper.AudioTrackMapper;
import com.nievierov.audio_server.service.AudioTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/app/v1/audio_tracks")
@RequiredArgsConstructor
public class AudioTrackController {

    private final AudioTrackService audioTrackService;
    private final AudioTrackMapper audioTrackMapper;

    @GetMapping("/{id}")
    public AudioTrackDto getAudioTrack(@PathVariable Long id) {
        var audioTrack = audioTrackService.getAudioTrackById(id);

        return audioTrackMapper.toDto(audioTrack);
    }

    @GetMapping
    public List<AudioTrackDto> getAllAudioTracks(@RequestParam UUID projectId) {
        var audioTracks = audioTrackService.getAllAudioTracks(projectId);

        return audioTracks.stream()
                .map(audioTrackMapper::toDto)
                .toList();
    }

    @PatchMapping("/{id}")
    public AudioTrackDto updateAudioTrack(@PathVariable Long id, @RequestBody AudioTrackUpdatedDto updatedDto) {
        return  audioTrackMapper.toDto(audioTrackService.updateAudioTrack(id, updatedDto));
    }

    @PostMapping
    public AudioTrackDto addAudioTrack(@RequestParam UUID projectId) {
        var savedAudioTrack = audioTrackService.addAudioTrack(projectId);

        return audioTrackMapper.toDto(savedAudioTrack);
    }

    @DeleteMapping("/{id}")
    public void deleteAudioTrack(@PathVariable Long id) {
        audioTrackService.deleteAudioTrackById(id);
    }
}
