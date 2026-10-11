package com.nievierov.audio_server.mapper;

import com.nievierov.audio_server.dto.AudioTrackUpdatedDto;
import com.nievierov.audio_server.dto.ProjectDto;
import com.nievierov.audio_server.dto.ProjectUpdatedDto;
import com.nievierov.audio_server.entity.AudioTrack;
import com.nievierov.audio_server.entity.Project;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDto toDto(Project project);
    Project toEntity(ProjectDto projectDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProjectUpdatedDto projectUpdatedDto, @MappingTarget Project project);
}
