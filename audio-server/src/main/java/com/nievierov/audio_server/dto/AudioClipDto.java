package com.nievierov.audio_server.dto;

public record AudioClipDto(
        Long id,
        String sourceFilePath,
        Long startTimeMs,
        Long durationMs,
        Long startOffsetMs,
        Long trackId
) {
}
