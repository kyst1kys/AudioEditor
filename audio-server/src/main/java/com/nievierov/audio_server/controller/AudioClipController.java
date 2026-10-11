package com.nievierov.audio_server.controller;

import com.nievierov.audio_server.dto.AudioClipDto;
import com.nievierov.audio_server.dto.AudioClipUpdatedDto;
import com.nievierov.audio_server.entity.AudioClip;
import com.nievierov.audio_server.mapper.AudioClipMapper;
import com.nievierov.audio_server.service.AudioClipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/v1/clips")
@RequiredArgsConstructor
public class AudioClipController {
    private final AudioClipService audioClipService;
    private final AudioClipMapper audioClipMapper;

    @GetMapping("/{id}")
    public AudioClipDto getAudioClipById(@PathVariable Long id) {
        var audioClip = audioClipService.getAudioClipById(id);

        return audioClipMapper.toDto(audioClip);
    }

    @GetMapping
    public List<AudioClipDto> getAllAudioClipsByTrackId(@RequestParam Long trackId) {
        List<AudioClip> audioClips = audioClipService.getAllAudioClips(trackId);

        return audioClips.stream()
                .map(audioClipMapper::toDto)
                .toList();
    }

    @PostMapping()
    public AudioClipDto addAudioClip(@RequestBody AudioClipDto audioClipDto) {
        return audioClipMapper.toDto(audioClipService.addAudioClip(audioClipDto));
    }

    @PatchMapping("/{id}")
    public AudioClipDto updateAudioClip(@PathVariable Long id, @RequestBody AudioClipUpdatedDto updatedClipDto) {
        return audioClipMapper.toDto(audioClipService.updateAudioClip(id, updatedClipDto));
    }

    @PutMapping
    public List<AudioClipDto> syncClips(@RequestBody List<AudioClipDto> clipsDto) {
        List<AudioClip> audioClips = audioClipService.syncAudioClips(clipsDto);

        return audioClips.stream()
                .map(audioClipMapper::toDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void deleteAudioClipById(@PathVariable Long id) {
        audioClipService.deleteAudioClip(id);
    }
}
