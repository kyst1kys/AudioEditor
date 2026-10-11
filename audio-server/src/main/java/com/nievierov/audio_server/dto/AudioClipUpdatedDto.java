package com.nievierov.audio_server.dto;

public record AudioClipUpdatedDto(
        String sourceFilePath,
        Long startTimeMs,
        Long durationMs,
        Long startOffsetMs,
        Long trackId
) {
}
