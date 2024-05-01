package ru.nsu.fit.geodrilling.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private double kanisotropyDown;

    private double roDown;

    private double kanisotropyUp;

    private double roUp;

    private double alpha;

    private double tvdStart;

    private Double startX;
    private Double endX;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @OneToMany(mappedBy = "modelEntity")
    private List<AreasEntity> areasEntity = new ArrayList<>();

    @OneToMany(mappedBy = "modelEntity")
    private List<CacheAreasEntity> CacheAreasEntity = new ArrayList<>();
}
