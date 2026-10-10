package com.nievierov.audio_server.dto;

public record AudioTrackUpdatedDto(
        Double volume,
        Boolean isMuted
) {
}
