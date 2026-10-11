package com.nievierov.audio_server.dto;

import java.util.UUID;

public record AudioTrackDto(
        Long id,
        double volume,
        boolean isMuted,
        UUID projectId
) {
}
