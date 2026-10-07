package com.nievierov.audio_editor.repository;

import com.nievierov.audio_editor.entity.AudioTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AudioTrackRepository extends JpaRepository<AudioTrack, Long> {
    List<AudioTrackRepository> findByProjectId(UUID projectId);
}
