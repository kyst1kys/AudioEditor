package com.nievierov.audio_server.controller;

import com.nievierov.audio_server.dto.ProjectDto;
import com.nievierov.audio_server.dto.ProjectUpdatedDto;
import com.nievierov.audio_server.entity.Project;
import com.nievierov.audio_server.mapper.ProjectMapper;
import com.nievierov.audio_server.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/app/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @GetMapping
    public List<ProjectDto> getAllProjects(){
        List<Project> projects = projectService.getAllProjects();

        return projects.stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ProjectDto getProjectById(@PathVariable UUID id){
        var project = projectService.getProjectById(id);

        return  projectMapper.toDto(project);
    }

    @PostMapping
    public ProjectDto createProject(@RequestBody ProjectDto projectDto){
        var savedProject = projectService.createProject(projectDto);

        return projectMapper.toDto(savedProject);
    }

    @PatchMapping("/{id}")
    public ProjectDto updateProject(@RequestBody ProjectUpdatedDto projectUpdatedDto, @PathVariable UUID id) {
        return projectMapper.toDto(projectService.updateProject(projectUpdatedDto, id));
    }

    @PostMapping("/open")
    public ProjectDto openProjectByPath(@RequestParam String folderPath){
        var project = projectService.openProjectByPath(folderPath);

        return projectMapper.toDto(project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable UUID id){
        projectService.deleteProjectById(id);
    }
}
