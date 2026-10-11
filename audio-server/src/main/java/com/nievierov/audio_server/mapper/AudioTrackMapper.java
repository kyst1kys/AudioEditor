package com.nievierov.audio_server.mapper;

import com.nievierov.audio_server.dto.AudioTrackDto;
import com.nievierov.audio_server.dto.AudioTrackUpdatedDto;
import com.nievierov.audio_server.entity.AudioTrack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AudioTrackMapper {
    AudioTrack toEntity(AudioTrackUpdatedDto audioTrackUpdatedDto);
    AudioTrack toEntity(AudioTrackDto audioTrackDto);

    @Mapping(source="project.id", target="projectId")
    AudioTrackDto toDto(AudioTrack audioTrack);
}
