package ru.nsu.fit.geodrilling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "projectState", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackEntity> tracks;

}
