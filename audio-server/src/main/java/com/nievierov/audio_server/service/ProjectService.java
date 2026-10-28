package com.nievierov.audio_server.service;

import com.nievierov.audio_server.dto.ProjectDto;
import com.nievierov.audio_server.dto.ProjectUpdatedDto;
import com.nievierov.audio_server.entity.Project;
import com.nievierov.audio_server.mapper.ProjectMapper;
import com.nievierov.audio_server.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public Project createProject(ProjectDto projectDto) {
        Path fullPath = Paths.get(projectDto.folderPath(), projectDto.name());

        if (projectRepository.existsByFolderPath(fullPath.toString())) {
            throw new IllegalArgumentException("Проєкт за шляхом '" + fullPath + "' вже існує в базі даних!");
        }

        try {
            if (!Files.exists(fullPath)){
                Files.createDirectories(fullPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не вдалося створити папку проєкту: " + fullPath, e);
        }



        var project = projectMapper.toEntity(projectDto);
        project.setFolderPath(fullPath.toString());



        return projectRepository.save(project);
    }

    public Project openProjectByPath(String folderPath) {
        return projectRepository.findByFolderPath(folderPath)
                .orElseGet(() -> {
                    String folderName = java.nio.file.Paths.get(folderPath).getFileName().toString();
                    Project newProject = new Project(folderName, folderPath);
                    return projectRepository.save(newProject);
                });
    }

    public Project updateProject(ProjectUpdatedDto updatedDto, UUID id) {
        var project = getProjectById(id);

        projectMapper.updateEntityFromDto(updatedDto, project);

        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!"));
    }

    public void deleteProjectById(UUID projectId) {
        projectRepository.deleteById(projectId);
    }
}
