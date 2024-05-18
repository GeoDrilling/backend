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
    private Boolean newProject;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "soot_id")
    private SootEntity sootEntity;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL)
    private List<ModelEntity> modelEntityList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "supplementing_project_id")
    private ProjectEntity supplementingProject;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ProjectState state;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SharedToken token;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MultiCurveEntity> multiCurves;
}
