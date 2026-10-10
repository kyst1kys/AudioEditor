package com.nievierov.audio_server.repository;

import com.nievierov.audio_server.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    public Optional<Project> findByProjectFolderPath(String projectFolderPath);
}
