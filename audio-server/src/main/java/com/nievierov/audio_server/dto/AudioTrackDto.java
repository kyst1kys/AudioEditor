package com.nievierov.audio_server.dto;

public record AudioTrackDto(
        Long id,
        double volume,
        boolean isMuted
) {
}
