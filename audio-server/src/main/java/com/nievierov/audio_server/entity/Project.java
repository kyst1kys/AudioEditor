package com.nievierov.audio_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "projects")
@Getter @Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String projectFolderPath;

    private LocalDateTime createdAt =  LocalDateTime.now();

    private int sampleRate = 44100;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<AudioTrack> audioTracks;

    public Project(String name, String projectFolderPath) {
        this.name = name;
        this.projectFolderPath = projectFolderPath;
    }
}
