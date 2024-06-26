package ru.nsu.fit.geodrilling.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="_curve")
public class CurveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="project_id")
    private ProjectEntity project;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String data;

    @Builder.Default
    private String dirInProject = "";

    @Builder.Default
    private Boolean isSynthetic = false;

    public String getFullName() {
        return dirInProject + name;
    }
}
