package ru.nsu.fit.geodrilling.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_las")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Column(length = 2000)
    private String curvesNames;
}
