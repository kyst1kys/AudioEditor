package com.nievierov.audio_server.service;

import com.nievierov.audio_server.dto.ProjectUpdatedDto;
import com.nievierov.audio_server.entity.Project;
import com.nievierov.audio_server.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project createProject(Project project) {
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

        if (updatedDto.name() != null) {
            project.setName(updatedDto.name());
        }
        if (updatedDto.sampleRate() != null) {
            project.setSampleRate(updatedDto.sampleRate());
        }

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
