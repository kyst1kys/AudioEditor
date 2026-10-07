package com.nievierov.audio_server.repository;

import com.nievierov.audio_server.entity.AudioClip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioClipRepository extends JpaRepository<AudioClip, Long> {
    List<AudioClip> findByAudioTrackId(Long audioTrackId);
}
