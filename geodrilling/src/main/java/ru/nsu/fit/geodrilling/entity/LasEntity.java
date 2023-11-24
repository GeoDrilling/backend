package ru.nsu.fit.geodrilling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_las")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Column(length = 2000)
    private String curvesNamesInJson;

    private String name;
    private Set<String> curvesNames = new HashSet<>();
}
