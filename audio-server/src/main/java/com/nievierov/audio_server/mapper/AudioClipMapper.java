package com.nievierov.audio_server.mapper;

import com.nievierov.audio_server.dto.AudioClipDto;
import com.nievierov.audio_server.dto.AudioClipUpdatedDto;
import com.nievierov.audio_server.entity.AudioClip;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AudioClipMapper {
    @Mapping(source = "audioTrack.id", target = "trackId")
    AudioClipDto toDto(AudioClip audioClip);
    AudioClip toEntity(AudioClipDto audioClipDto);

    // for PATCH
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AudioClipUpdatedDto audioClipUpdatedDto, @MappingTarget AudioClip audioClip);

    // for PUT
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AudioClipDto audioClipDto, @MappingTarget AudioClip audioClip);
}
