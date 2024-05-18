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

    public ModelEntity(ModelEntity d) {
        if (d == null) {
            throw new IllegalArgumentException("Cannot copy, 'd' is null");
        }
        // Копирование всех полей из объекта 'd'
        this.name = d.name;
        this.kanisotropyDown = d.kanisotropyDown;
        this.roDown = d.roDown;
        this.kanisotropyUp = d.kanisotropyUp;
        this.roUp = d.roUp;
        this.alpha = d.alpha;
        this.tvdStart = d.tvdStart;
        this.startX = d.startX;
        this.endX = d.endX;
    }
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @OneToMany(mappedBy = "modelEntity", cascade = CascadeType.ALL)
    private List<AreasEntity> areasEntity = new ArrayList<>();

    @OneToMany(mappedBy = "modelEntity", cascade = CascadeType.ALL)
    private List<CacheAreasEntity> CacheAreasEntity = new ArrayList<>();


}
