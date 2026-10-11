package com.nievierov.audio_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "audio_tracks")
@Getter @Setter
@NoArgsConstructor
public class AudioTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double volume = 1.0;

    private Boolean isMuted = false;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "audioTrack",  cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<AudioClip> audioClips;

    public AudioTrack(Project project) {
        this.project = project;
    }

}
