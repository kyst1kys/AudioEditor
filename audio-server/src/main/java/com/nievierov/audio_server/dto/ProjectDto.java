package com.nievierov.audio_server.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectDto(
        UUID id,
        String name,
        String folderPath,
        Integer sampleRate,
        LocalDateTime createdAt
) {}
