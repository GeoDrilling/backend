package ru.nsu.fit.geodrilling.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.geodrilling.model.User;

import java.util.*;

@Entity
@Table(name = "_projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurveEntity> curves = new ArrayList<>();

    private Boolean readOnly = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soot_id")
    private SootEntity sootEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private ModelEntity modelEntity;

    private Long supplementedProjectId = null;
}
