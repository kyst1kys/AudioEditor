package com.nievierov.audio_server.mapper;

import com.nievierov.audio_server.dto.AudioTrackDto;
import com.nievierov.audio_server.dto.AudioTrackUpdatedDto;
import com.nievierov.audio_server.entity.AudioTrack;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AudioTrackMapper {
    AudioTrack toEntity(AudioTrackUpdatedDto audioTrackUpdatedDto);
    AudioTrack toEntity(AudioTrackDto audioTrackDto);
    AudioTrackDto toDto(AudioTrack audioTrack);
}
