package ru.nsu.fit.geodrilling.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.geodrilling.model.User;

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

    @OneToOne(mappedBy = "modelEntity")
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "areas_id")
    private AreasEntity areasEntity;

}
