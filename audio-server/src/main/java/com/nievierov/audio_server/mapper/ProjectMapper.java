package com.nievierov.audio_server.mapper;

import com.nievierov.audio_server.dto.ProjectDto;
import com.nievierov.audio_server.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDto toDto(Project project);
    Project toEntity(ProjectDto projectDto);
}
