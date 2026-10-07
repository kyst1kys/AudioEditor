package com.nievierov.audio_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "audio_clips")
@Getter
@Setter
@NoArgsConstructor
public class AudioClip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceFilePath;

    private long timelineStartMs;

    private long durationMs;

    @ManyToOne
    @JoinColumn(name = "audio_track")
    private AudioTrack audioTrack;

    public AudioClip(String sourceFilePath, AudioTrack audioTrack) {
        this.sourceFilePath = sourceFilePath;
        this.audioTrack = audioTrack;
    }
}
