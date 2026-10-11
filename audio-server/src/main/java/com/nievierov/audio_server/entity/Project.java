package com.nievierov.audio_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
    private String folderPath;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Integer sampleRate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<AudioTrack> audioTracks;

    public Project(String name, String folderPath) {
        this.name = name;
        this.folderPath = folderPath;
    }
}
