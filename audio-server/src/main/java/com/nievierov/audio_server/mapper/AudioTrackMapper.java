package com.nievierov.audio_server.mapper;

import com.nievierov.audio_server.dto.AudioTrackDto;
import com.nievierov.audio_server.dto.AudioTrackUpdatedDto;
import com.nievierov.audio_server.entity.AudioTrack;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AudioTrackMapper {
    @Mapping(source="project.id", target="projectId")
    AudioTrackDto toDto(AudioTrack audioTrack);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AudioTrackUpdatedDto audioTrackUpdatedDto, @MappingTarget AudioTrack audioTrack);
}
